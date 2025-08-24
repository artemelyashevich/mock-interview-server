package com.mock.interview.application.service;

import com.mock.interview.application.port.in.InterviewQuestionService;
import com.mock.interview.application.port.in.InterviewService;
import com.mock.interview.application.port.out.InterviewQuestionRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewService interviewService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public InterviewQuestionModel create(Long questionId, Long userId, InterviewQuestionModel interviewQuestionModel) {
        log.debug("Attempting to create interview question");

        var interviewQuestion = interviewQuestionRepository.save(interviewQuestionModel);

        interviewService.addQuestion(userId, questionId, interviewQuestion);

        log.debug("Created interview question");
        return interviewQuestion;
    }

    @Override
    @Transactional
    public InterviewQuestionModel saveAnswer(Long questionId, Long userId, String answer) {
        log.debug("Attempting to save interview question");

        var question = interviewQuestionRepository.findById(questionId);

        if (question.getAnswerText().equals(answer)) {
            var message = "Answer already exists";
            log.debug(message);
            throw new ResourceNotFoundException(message);
        }

        question.setAnswerText(answer);

        var updated = interviewQuestionRepository.save(question);

        log.debug("Updated interview question");
        return updated;
    }

    @Override
    @Transactional
    public InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId) {
        var question = interviewQuestionRepository.findById(questionId);

        question.evaluate(evaluation);

        updateInterviewOverallScore(question.getInterviewId().value());

        var savedQuestion = interviewQuestionRepository.save(question);

        log.debug("Updated interview question evaluation");
        return savedQuestion;
    }

    @Override
    public InterviewQuestionModel findCurrentQuestion(Long interviewId) {
        log.debug("Attempting to find current interview question");

        var question = interviewQuestionRepository.findNextQuestion(interviewId);

        log.debug("Found current interview question");
        return question;
    }

    private void updateInterviewOverallScore(Long interviewId) {
        var averageScore = interviewQuestionRepository.calculateAverageScoreByInterviewId(interviewId);

        var interview = interviewService.findById(interviewId);

        interview.setOverallScore(averageScore);

        interviewService.save(interview);
    }
}
