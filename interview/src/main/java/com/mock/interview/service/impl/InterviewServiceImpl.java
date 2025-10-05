package com.mock.interview.service.impl;

import com.mock.interview.entity.InterviewEntity;
import com.mock.interview.lib.contract.CommonNotificationService;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.lib.specification.GenericSpecificationRepository;
import com.mock.interview.lib.specification.GenericSpecificationService;
import com.mock.interview.mapper.InterviewEntityMapper;
import com.mock.interview.repository.InterviewRepository;
import com.mock.interview.service.InterviewService;
import com.mock.interview.service.InterviewTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl extends GenericSpecificationService<InterviewEntity, Long> implements InterviewService {

    private static final InterviewEntityMapper mapper = InterviewEntityMapper.INSTANCE;

    private final InterviewRepository interviewEntityRepository;
    private final CommonNotificationService<InterviewModel> notificationService;
    private final InterviewTemplateService interviewTemplateService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Caching(
            put = {
                    @CachePut(value="InterviewService::findById", key = "#interview.id()"),
            }
    )
    public InterviewModel create(InterviewModel interviewModel) {
        log.debug("Create Interview");

        log.debug("Created Interview");
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    @Caching(
            put = {
                    @CachePut(value="InterviewService::findById", key = "#interviewId"),
            }
    )
    public InterviewModel addQuestion(Long interviewId, Long userId, InterviewQuestionModel interviewQuestionModel) {
        log.debug("Add Question");

        log.debug("Updated Interview");
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel startInterview(Long interviewId, Long userId) {
        log.debug("Start interview with id {}", interviewId);

        log.debug("Interview started");
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel completeInterview(Long interviewId, Long userId) {
        log.debug("Complete interview with id {}", interviewId);

        log.debug("Interview completed");
        return null;
    }

    @Override
    @Cacheable(value="InterviewService::findById", key = "#interviewId")
    public InterviewModel findById(Long interviewId) {
        log.debug("Attempt to find interview with id {}", interviewId);
        log.debug("Interview found");
        return null;
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value="InterviewService::findById", key = "#interviewId"),
            }
    )
    public void save(InterviewModel interviewModel) {
        log.debug("Save Interview");

        log.debug("Update Interview");
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
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected GenericSpecificationRepository<InterviewEntity, Long> getRepository() {
        return interviewEntityRepository;
    }
}