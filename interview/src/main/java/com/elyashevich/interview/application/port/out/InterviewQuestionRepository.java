package com.elyashevich.interview.application.port.out;

import com.mock.interview.lib.model.InterviewQuestionModel;

import java.util.List;

public interface InterviewQuestionRepository {

    List<InterviewQuestionModel> findAll();

    InterviewQuestionModel findById(Long id);

    List<InterviewQuestionModel> findByInterviewId(Long id);

    InterviewQuestionModel save(InterviewQuestionModel interviewQuestionModel);

    void delete(InterviewQuestionModel interviewQuestionModel);
}
