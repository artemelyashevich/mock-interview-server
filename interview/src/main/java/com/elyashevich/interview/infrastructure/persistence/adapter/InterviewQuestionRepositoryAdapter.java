package com.elyashevich.interview.infrastructure.persistence.adapter;

import com.elyashevich.interview.application.port.out.InterviewQuestionRepository;
import com.elyashevich.interview.infrastructure.persistence.mapper.InterviewQuestionEntityMapper;
import com.elyashevich.interview.infrastructure.persistence.repository.InterviewQuestionEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.InterviewQuestionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewQuestionRepositoryAdapter implements InterviewQuestionRepository {

    private static final InterviewQuestionEntityMapper MAPPER = InterviewQuestionEntityMapper.INSTANCE;
    private static final String NO_QUESTIONS_FOUND_MSG = "No questions found for interview id: %d";

    private final InterviewQuestionEntityRepository repository;

    @Override
    public List<InterviewQuestionModel> findAllByInterviewId(Long interviewId) {
        validateInterviewId(interviewId);
        return MAPPER.toModels(repository.findAllByInterviewId(interviewId));
    }

    @Override
    public List<InterviewQuestionModel> findAllByInterviewIdAndTopic(Long interviewId, String topic) {
        validateInterviewId(interviewId);
        validateTopic(topic);

        return MAPPER.toModels(
            repository.findAllByInterviewIdAndTopicContaining(interviewId, topic.trim())
        );
    }

    @Override
    public InterviewQuestionModel save(InterviewQuestionModel question) {
        Objects.requireNonNull(question, "Question model cannot be null");
        return MAPPER.toModel(repository.save(MAPPER.toEntity(question)));
    }

    @Override
    public void deleteAllByInterviewId(Long interviewId) {
        validateInterviewId(interviewId);
        repository.deleteAllByInterviewId(interviewId);
    }

    @Override
    public InterviewQuestionModel findLatestByInterviewId(Long interviewId) {
        validateInterviewId(interviewId);
        return repository.findLatestByInterviewId(interviewId)
            .map(MAPPER::toModel)
            .orElseThrow(() -> createNotFoundException(interviewId));
    }

    @Override
    public InterviewQuestionModel findNextQuestion(Long interviewId) {
        validateInterviewId(interviewId);
        return repository.findNextQuestion(interviewId)
            .map(MAPPER::toModel)
            .orElseThrow(() -> createNotFoundException(interviewId));
    }

    @Override
    public Long countByInterviewId(Long interviewId) {
        validateInterviewId(interviewId);
        return repository.countByInterviewId(interviewId);
    }

    @Override
    public Double calculateAverageScoreByInterviewId(Long interviewId) {
        validateInterviewId(interviewId);
        return repository.calculateAverageScoreByInterviewId(interviewId);
    }

    private ResourceNotFoundException createNotFoundException(Long interviewId) {
        String message = String.format(NO_QUESTIONS_FOUND_MSG, interviewId);
        log.debug(message);
        return new ResourceNotFoundException(message);
    }

    private void validateInterviewId(Long interviewId) {
        if (interviewId == null || interviewId <= 0) {
            throw new IllegalArgumentException("Interview ID must be positive");
        }
    }

    private void validateTopic(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
    }
}
