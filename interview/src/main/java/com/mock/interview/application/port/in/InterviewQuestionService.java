package com.mock.interview.application.port.in;

import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewQuestionModel;

public interface InterviewQuestionService {

    InterviewQuestionModel create(Long questionId, Long userId, InterviewQuestionModel interviewQuestionModel);

    InterviewQuestionModel saveAnswer(Long questionId, Long userId, String answer);

    InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId);


    InterviewQuestionModel findCurrentQuestion(Long interviewId);
}
