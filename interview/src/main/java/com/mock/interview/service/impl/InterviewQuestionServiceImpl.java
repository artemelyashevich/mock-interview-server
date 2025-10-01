package com.mock.interview.service.impl;

import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.repository.InterviewQuestionRepository;
import com.mock.interview.service.InterviewQuestionService;
import com.mock.interview.service.InterviewService;
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

        log.debug("Created interview question");
        return null;
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

        log.debug("Updated interview question");
        return null;
    }

    @Override
    @Transactional
    @Cacheable(value="InterviewQuestionService::evaluateQuestion", key = "#questionId")
    public InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId) {
        log.debug("Updated interview question evaluation");
        return null;
    }

    @Override
    @Cacheable(value="InterviewQuestionService::findCurrentQuestion", key = "#interviewId")
    public InterviewQuestionModel findCurrentQuestion(Long interviewId) {
        log.debug("Attempting to find current interview question");

        log.debug("Found current interview question");
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long interviewId) {
        log.debug("Attempting to delete interview question: {}", interviewId);


        log.debug("Deleted interview question");
    }

    private void updateInterviewOverallScore(Long interviewId) {
    }
}
