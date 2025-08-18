package com.elyashevich.interview.application.service;

import com.elyashevich.interview.application.port.in.InterviewService;
import com.elyashevich.interview.application.port.in.NotificationService;
import com.elyashevich.interview.application.port.out.InterviewRepository;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.model.NotificationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewEntityRepository;
    private final NotificationService notificationService;

    // TODO
    @Override
    public InterviewModel create(InterviewModel interviewModel) {
        log.debug("Create Interview");

        var interview = interviewEntityRepository.save(interviewModel);
        notificationService.send(NotificationModel.builder().build());
        return null;
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
        interviewEntityRepository.save(interview);

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
}
