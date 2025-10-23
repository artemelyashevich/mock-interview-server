package com.mock.interview.service.interview.impl;

import com.mock.interview.entity.InterviewEntity;
import com.mock.interview.lib.contract.CommonNotificationService;
import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewReportStatusModel;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.lib.model.ReportFormat;
import com.mock.interview.lib.model.ReportStatus;
import com.mock.interview.lib.specification.GenericSpecificationRepository;
import com.mock.interview.lib.specification.GenericSpecificationService;
import com.mock.interview.lib.util.AsyncHelper;
import com.mock.interview.lib.util.JsonHelper;
import com.mock.interview.mapper.InterviewEntityMapper;
import com.mock.interview.repository.InterviewRepository;
import com.mock.interview.service.interview.InterviewQuestionService;
import com.mock.interview.service.interview.InterviewService;
import com.mock.interview.service.interview.InterviewTemplateService;
import com.mock.interview.service.web.ReportWebClientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl extends GenericSpecificationService<InterviewEntity, Long> implements InterviewService {

    private static final InterviewEntityMapper mapper = InterviewEntityMapper.INSTANCE;
    private static final String KEY_PREFIX = "interview-report-%s";

    private final InterviewRepository interviewEntityRepository;
    private final CommonNotificationService<InterviewModel> notificationService;
    private final InterviewTemplateService interviewTemplateService;
    private final InterviewQuestionService interviewQuestionService;
    private final ReportWebClientService reportWebClientService;

    private final BlockingQueue<InterviewModel> queue;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Caching(
            put = {
                    @CachePut(value = "InterviewService::findById", key = "#interview.id()"),
            }
    )
    public InterviewModel create(InterviewModel interviewModel) {
        log.debug("Create Interview");
        var interviewEntity = interviewEntityRepository.save(mapper.toEntity(interviewModel));
        log.debug("Created Interview");
        return mapper.toModel(interviewEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @Caching(
            put = {
                    @CachePut(value = "InterviewService::findById", key = "#interviewId"),
            }
    )
    public InterviewModel addQuestions(Long interviewId, Long userId) {
        log.debug("Add Question");
        var interviewEntity = findById(interviewId);
        var interviewTemplate = interviewTemplateService.findById(interviewEntity.getTemplateId());
        interviewQuestionService.setQuestions(interviewTemplate, interviewEntity);
        interviewEntity.setStatus(InterviewStatus.FILLED);
        interviewEntityRepository.save(mapper.toEntity(interviewEntity));
        log.debug("Updated Interview");
        return interviewEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel startInterview(Long interviewId, Long userId) {
        log.debug("Start interview with id {}", interviewId);
        var interviewEntity = findById(interviewId);
        if (!interviewEntity.getStatus().equals(InterviewStatus.FILLED)) {
            throw new MockInterviewException("Interview has '%s' status!".formatted(interviewEntity.getStatus()), 400);
        }
        interviewEntity.setStatus(InterviewStatus.IN_PROGRESS);
        log.debug("Interview started");
        return mapper.toModel(interviewEntityRepository.save(mapper.toEntity(interviewEntity)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel completeInterview(Long interviewId, Long userId) {
        log.debug("Complete interview with id {}", interviewId);
        var interviewEntity = findById(interviewId);
        if (!interviewEntity.getStatus().equals(InterviewStatus.IN_PROGRESS)) {
            throw new MockInterviewException("Interview in status: '%s'".formatted(interviewEntity.getStatus()), 500);
        }
        interviewEntity.setStatus(InterviewStatus.COMPLETED);
        AsyncHelper.runAsync(() -> {
            reportWebClientService.saveReport(CreateReportRequest.builder()
                    .title("Interview evaluation")
                    .interviewId(interviewId)
                    .format(ReportFormat.PDF)
                    .build());
            redisTemplate.opsForValue().set(String.format(KEY_PREFIX, interviewId), Objects.requireNonNull(JsonHelper.toJson(
                    InterviewReportStatusModel.builder()
                            .reportStatus(ReportStatus.PENDING)
                            .build()
            )));
            queue.add(interviewEntity);
        });
        log.debug("Interview completed");
        return mapper.toModel(interviewEntityRepository.save(mapper.toEntity(interviewEntity)));
    }

    @Override
    @Cacheable(value = "InterviewService::findById", key = "#interviewId")
    public InterviewModel findById(Long interviewId) {
        log.debug("Attempt to find interview with id {}", interviewId);
        var interview = interviewEntityRepository.findById(interviewId).orElseThrow(
                () -> {
                    var message = "Interview with id: '%s' was not found".formatted(interviewId);
                    log.error(message);
                    return new MockInterviewException(message, 404);
                }
        );
        log.debug("Interview found");
        return mapper.toModel(interview);
    }

    @Override
    public InterviewModel findByTemplate(InterviewTemplateModel interviewTemplate) {
        log.debug("Attempting find interview by template {}", interviewTemplate);

        var interview = interviewEntityRepository.findByTemplateId(interviewTemplate.getId());
        return mapper.toModel(interview);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<InterviewModel>> batchSaveAsync(List<InterviewModel> interviews) {
        log.debug("Batch saving {} interviews asynchronously", interviews.size());

        return CompletableFuture.supplyAsync(() ->
                interviews.stream()
                        .map(interview -> {
                            try {
                                return mapper.toModel(interviewEntityRepository.save(mapper.toEntity(interview)));
                            } catch (Exception e) {
                                log.error("Failed to save interview {}: {}", interview.getId(), e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList());
    }

    @Override
    protected GenericSpecificationRepository<InterviewEntity, Long> getRepository() {
        return interviewEntityRepository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void evaluate() {
        if (queue.isEmpty()) {
            return;
        }
        var retryQueue = new LinkedBlockingQueue<InterviewModel>();
        while (!queue.isEmpty()) {
            AsyncHelper.runAsync(() -> {
                var interview = queue.poll();
                if (interview == null) {
                    return;
                }
                var key = KEY_PREFIX.formatted(interview.getId());
                var response = (InterviewReportStatusModel) JsonHelper.fromJson(
                        redisTemplate.opsForValue().get(key),
                        InterviewReportStatusModel.class);
                if (response != null && response.getReportStatus().equals(ReportStatus.COMPLETED)) {
                    interview.setStatus(InterviewStatus.EVALUATED);
                    interview.setReportId(response.getReportId());
                    var res = interviewEntityRepository.save(mapper.toEntity(interview));
                    redisTemplate.execute(new SessionCallback<>() {
                        @Override
                        @SuppressWarnings("unchecked")
                        public Object execute(@NonNull RedisOperations operations) throws DataAccessException {
                            operations.multi();

                            notificationService.send(mapper.toModel(res));
                            operations.delete(key);

                            operations.exec();
                            return null;
                        }
                    });
                } else {
                    retryQueue.add(interview);
                }
            });
        }
        queue.addAll(retryQueue);
        retryQueue.clear();
    }
}