package com.elyashevich.interview.application.service;

import com.elyashevich.interview.application.port.in.InterviewService;
import com.elyashevich.interview.application.port.in.InterviewTemplateService;
import com.elyashevich.interview.application.port.in.NotificationService;
import com.elyashevich.interview.application.port.out.InterviewRepository;
import com.elyashevich.interview.infrastructure.persistence.entity.InterviewEntity;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.specification.GenericSpecificationRepository;
import com.mock.interview.lib.specification.GenericSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl extends GenericSpecificationService<InterviewEntity, Long> implements InterviewService {

    private final InterviewRepository interviewEntityRepository;
    private final NotificationService notificationService;
    private final InterviewTemplateService interviewTemplateService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InterviewModel create(InterviewModel interviewModel) {
        log.debug("Create Interview");

        var interviewTemplate = interviewTemplateService.findById(interviewModel.getTemplateId());

        var interview = interviewEntityRepository.save(InterviewModel.builder()
                .templateId(interviewTemplate.getId())
                .status(InterviewStatus.IN_PROGRESS)
                .userId(interviewModel.getUserId())
                .build());

        notificationService.sendNotificationAsync(interview);

        log.debug("Created Interview");
        return interview;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public InterviewModel addQuestion(Long interviewId, Long userId, InterviewQuestionModel interviewQuestionModel) {
        log.debug("Add Question");

        var interview = interviewEntityRepository.findByIdAndUserId(interviewId, userId);

        if (interview.getQuestions().stream().anyMatch(interviewQuestionModel::equals)) {
            var message = "Question already exists";
            log.debug(message);
            throw new ResourceAlreadyExistException(message);
        }

        interview.getQuestions().add(interviewQuestionModel);

        var updatedInterview = interviewEntityRepository.save(interview);

        log.debug("Updated Interview");
        return updatedInterview;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel startInterview(Long interviewId, Long userId) {
        log.debug("Start interview with id {}", interviewId);

        var interview = interviewEntityRepository.findByIdAndUserId(interviewId, userId);

        if (interview.getStatus().equals(InterviewStatus.IN_PROGRESS)) {
            log.debug("Interview already started");
            throw new MockInterviewException(400);
        }

        interview.setStatus(InterviewStatus.IN_PROGRESS);
        interview.setStartTime(LocalDateTime.now());

        interviewEntityRepository.save(interview);

        log.debug("Interview started");
        return interview;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public InterviewModel completeInterview(Long interviewId, Long userId) {
        log.debug("Complete interview with id {}", interviewId);

        var interview = interviewEntityRepository.findByIdAndUserId(interviewId, userId);

        if (interview.getStatus().equals(InterviewStatus.COMPLETED)) {
            log.debug("Interview already completed");
            throw new MockInterviewException(400);
        }

        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setEndTime(LocalDateTime.now());

        interviewEntityRepository.save(interview);

        notificationService.sendNotificationAsync(interview);

        log.debug("Interview completed");
        return interview;
    }

    @Override
    public InterviewModel findById(Long interviewId) {
        log.debug("Attempt to find interview with id {}", interviewId);
        var interview = interviewEntityRepository.findById(interviewId);
        log.debug("Interview found");
        return interview;
    }

    @Override
    public void save(InterviewModel interviewModel) {
        log.debug("Save Interview");

        var interview = findById(interviewModel.getId().value());
        interview.setStatus(interviewModel.getStatus());
        interview.setOverallScore(interviewModel.getOverallScore());

        interviewEntityRepository.save(interview);
        log.debug("Update Interview");
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<InterviewModel>> batchSaveAsync(List<InterviewModel> interviews) {
        log.debug("Batch saving {} interviews asynchronously", interviews.size());

        return CompletableFuture.supplyAsync(() ->
                interviews.stream()
                        .map(interview -> {
                            try {
                                return interviewEntityRepository.save(interview);
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
    @SuppressWarnings("unchecked")
    protected GenericSpecificationRepository<InterviewEntity, Long> getRepository() {
        return (GenericSpecificationRepository<InterviewEntity, Long>) interviewEntityRepository;
    }
}