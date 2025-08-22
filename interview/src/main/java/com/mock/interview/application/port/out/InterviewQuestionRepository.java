package com.mock.interview.application.port.out;

import com.mock.interview.lib.model.InterviewQuestionModel;

import java.util.List;

public interface InterviewQuestionRepository {

    List<InterviewQuestionModel> findAllByInterviewId(Long interviewId);

    List<InterviewQuestionModel> findAllByInterviewIdAndTopic(Long interviewId, String topic);

    InterviewQuestionModel save(InterviewQuestionModel question);

    void deleteAllByInterviewId(Long interviewId);

    InterviewQuestionModel findLatestByInterviewId(Long interviewId);

    InterviewQuestionModel findNextQuestion(Long interviewId);

    Long countByInterviewId(Long interviewId);

    Double calculateAverageScoreByInterviewId(Long interviewId);

    InterviewQuestionModel findById(Long id);
}
