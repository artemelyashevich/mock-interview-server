package com.elyashevich.interview.application.port.in;

import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;

public interface InterviewService {

    InterviewModel create(InterviewModel interviewModel);

    InterviewModel addQuestion(Long interviewId, Long userId, InterviewQuestionModel interviewQuestionModel);

    InterviewModel startInterview(Long interviewId, Long userId);

    InterviewModel completeInterview(Long interviewId, Long userId);

    InterviewModel findById(Long interviewId);

    void save(InterviewModel interview);
}
