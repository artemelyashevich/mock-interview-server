package com.elyashevich.interview.application.port.out;

import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRepository {

    List<InterviewModel> findAllByStatus(InterviewStatus status);

    List<InterviewModel> findAllByUserIdAndStatus(Long userId, InterviewStatus status);

    InterviewModel findByIdAndUserId(Long id, Long userId);

    List<InterviewModel> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<InterviewModel> findAllCompletedBeforeDate(LocalDateTime endTime, InterviewStatus status);

    Long countByUserIdAndStatus(Long userId, InterviewStatus status);

    InterviewModel save(InterviewModel interviewModel);

    void delete(InterviewModel interviewModel);
}
