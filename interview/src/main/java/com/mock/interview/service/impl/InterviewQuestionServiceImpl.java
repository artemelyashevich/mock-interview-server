package com.mock.interview.service.impl;

import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.repository.InterviewQuestionEntityRepository;
import com.mock.interview.service.InterviewQuestionService;
import com.mock.interview.service.InterviewService;
import com.mock.interview.service.mapper.InterviewQuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
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

    private static final InterviewQuestionMapper mapper = Mappers.getMapper(InterviewQuestionMapper.class);

    private final InterviewQuestionEntityRepository interviewQuestionRepository;
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

        var interviewQuestion = mapper.toModel(interviewQuestionRepository.save(mapper.toEntity(interviewQuestionModel)));

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

        var question = mapper.toModel(interviewQuestionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new));

        if (question.getAnswerText().equals(answer)) {
            var message = "Answer already exists";
            log.debug(message);
            throw new ResourceNotFoundException(message);
        }

        question.setAnswerText(answer);

        var updated = interviewQuestionRepository.save(mapper.toEntity(question));

        log.debug("Updated interview question");
        return mapper.toModel(updated);
    }

    @Override
    @Transactional
    @Cacheable(value="InterviewQuestionService::evaluateQuestion", key = "#questionId")
    public InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId) {
        var question = mapper.toModel(interviewQuestionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new));

        question.evaluate(evaluation);

        updateInterviewOverallScore(question.getInterviewId().value());

        var savedQuestion = interviewQuestionRepository.save(mapper.toEntity(question));

        log.debug("Updated interview question evaluation");
        return mapper.toModel(savedQuestion);
    }

    @Override
    @Cacheable(value="InterviewQuestionService::findCurrentQuestion", key = "#interviewId")
    public InterviewQuestionModel findCurrentQuestion(Long interviewId) {
        log.debug("Attempting to find current interview question");

        var question = interviewQuestionRepository.findNextQuestion(interviewId).orElseThrow(ResourceNotFoundException::new);

        log.debug("Found current interview question");
        return mapper.toModel(question);
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
