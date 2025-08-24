package com.mock.interview.application.service;

import com.mock.interview.application.port.in.InterviewQuestionService;
import com.mock.interview.application.port.in.InterviewService;
import com.mock.interview.application.port.out.InterviewQuestionRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Caching(
            put = {
                    @CachePut(value="InterviewQuestionService::evaluateQuestion", key = "#questionId"),
                    @CachePut(value="InterviewQuestionService::findCurrentQuestion", key = "#interviewQuestion.getId()")
            }
    )
    public InterviewQuestionModel create(Long questionId, Long userId, InterviewQuestionModel interviewQuestionModel) {
        log.debug("Attempting to create interview question");

        var interviewQuestion = interviewQuestionRepository.save(interviewQuestionModel);

        interviewService.addQuestion(userId, questionId, interviewQuestion);

        log.debug("Created interview question");
        return interviewQuestion;
    }

    @Override
    @Transactional
    @Caching(
            put = {
                    @CachePut(value="InterviewQuestionService::evaluateQuestion", key = "#questionId"),
                    @CachePut(value="InterviewQuestionService::findCurrentQuestion", key = "#interviewQuestion.getId()")
            }
    )
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
    @Cacheable(value="InterviewQuestionService::evaluateQuestion", key = "#questionId")
    public InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId) {
        var question = interviewQuestionRepository.findById(questionId);

        question.evaluate(evaluation);

        updateInterviewOverallScore(question.getInterviewId().value());

        var savedQuestion = interviewQuestionRepository.save(question);

        log.debug("Updated interview question evaluation");
        return savedQuestion;
    }

    @Override
    @Cacheable(value="InterviewQuestionService::findCurrentQuestion", key = "#interviewId")
    public InterviewQuestionModel findCurrentQuestion(Long interviewId) {
        log.debug("Attempting to find current interview question");

        var question = interviewQuestionRepository.findNextQuestion(interviewId);

        log.debug("Found current interview question");
        return question;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long interviewId) {
        log.debug("Attempting to delete interview question: {}", interviewId);

        if (interviewQuestionRepository.findById(interviewId) == null) {
            throw new ResourceNotFoundException("No interview question found with id: " + interviewId);
        }

        interviewQuestionRepository.deleteAllByInterviewId(interviewId);
        log.debug("Deleted interview question");
    }

    private void updateInterviewOverallScore(Long interviewId) {
        var averageScore = interviewQuestionRepository.calculateAverageScoreByInterviewId(interviewId);

        var interview = interviewService.findById(interviewId);

        interview.setOverallScore(averageScore);

        interviewService.save(interview);
    }
}
