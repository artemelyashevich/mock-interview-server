package com.mock.interview.infrastructure.persistence.adapter;

import com.mock.interview.application.port.out.InterviewRepository;
import com.mock.interview.infrastructure.persistence.mapper.InterviewEntityMapper;
import com.mock.interview.infrastructure.persistence.repository.InterviewEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewRepositoryAdapter implements InterviewRepository {

    private static final InterviewEntityMapper INTERVIEW_ENTITY_MAPPER = InterviewEntityMapper.INSTANCE;
    private static final String INTERVIEW_NOT_FOUND_MSG = "Cannot find interview with id %d and user id %d";

    private final InterviewEntityRepository interviewEntityRepository;

    @Override
    public List<InterviewModel> findAllByStatus(InterviewStatus status) {
        Objects.requireNonNull(status, "Interview status cannot be null");
        return INTERVIEW_ENTITY_MAPPER.toModels(
            interviewEntityRepository.findAllByStatus(status)
        );
    }

    @Override
    public List<InterviewModel> findAllByUserIdAndStatus(Long userId, InterviewStatus status) {
        validateUserId(userId);
        Objects.requireNonNull(status, "Interview status cannot be null");

        return INTERVIEW_ENTITY_MAPPER.toModels(
            interviewEntityRepository.findAllByUserIdAndStatus(userId, status)
        );
    }

    @Override
    public InterviewModel findByIdAndUserId(Long id, Long userId) throws ResourceNotFoundException {
        validateInterviewId(id);
        validateUserId(userId);

        return interviewEntityRepository.findByIdAndUserId(id, userId)
            .map(INTERVIEW_ENTITY_MAPPER::toModel)
            .orElseThrow(() -> {
                String message = String.format(INTERVIEW_NOT_FOUND_MSG, id, userId);
                log.debug(message);
                return new ResourceNotFoundException(message);
            });
    }

    @Override
    public List<InterviewModel> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "Start time cannot be null");
        Objects.requireNonNull(end, "End time cannot be null");
        validateTimeRange(start, end);

        return INTERVIEW_ENTITY_MAPPER.toModels(
            interviewEntityRepository.findAllInTimeRange(start, end)
        );
    }

    @Override
    public List<InterviewModel> findAllCompletedBeforeDate(LocalDateTime endTime, InterviewStatus status) {
        Objects.requireNonNull(endTime, "End time cannot be null");
        Objects.requireNonNull(status, "Interview status cannot be null");

        return INTERVIEW_ENTITY_MAPPER.toModels(
            interviewEntityRepository.findCompletedBeforeDate(endTime, status)
        );
    }

    @Override
    public Long countByUserIdAndStatus(Long userId, InterviewStatus status) {
        validateUserId(userId);
        Objects.requireNonNull(status, "Interview status cannot be null");

        return interviewEntityRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public InterviewModel save(InterviewModel interviewModel) {
        Objects.requireNonNull(interviewModel, "Interview model cannot be null");

        return INTERVIEW_ENTITY_MAPPER.toModel(
            interviewEntityRepository.save(INTERVIEW_ENTITY_MAPPER.toEntity(interviewModel))
        );
    }

    @Override
    public void delete(InterviewModel interviewModel) {
        Objects.requireNonNull(interviewModel, "Interview model cannot be null");
        Objects.requireNonNull(interviewModel.getId(), "Interview id cannot be null");

        interviewEntityRepository.deleteById(interviewModel.getId().value());
    }

    @Override
    public InterviewModel findById(Long interviewId) {
        return interviewEntityRepository.findById(interviewId)
            .map(INTERVIEW_ENTITY_MAPPER::toModel)
            .orElseThrow(()-> {
                var message = String.format(INTERVIEW_NOT_FOUND_MSG, interviewId);
                log.debug(message);
                return new ResourceNotFoundException(message);
            });
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User id must be positive");
        }
    }

    private void validateInterviewId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Interview id must be positive");
        }
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }
}
