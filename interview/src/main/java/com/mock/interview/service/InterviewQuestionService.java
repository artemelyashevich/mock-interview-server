package com.mock.interview.service;

import com.mock.interview.lib.model.EvaluationModel;
import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface InterviewQuestionService {

    List<InterviewQuestionModel> batchCreate(List<InterviewQuestionModel> interviewQuestionModel) throws ExecutionException, InterruptedException;

    InterviewQuestionModel create(Long questionId, Long userId, InterviewQuestionModel interviewQuestionModel);

    InterviewQuestionModel saveAnswer(Long questionId, Long userId, String answer);

    InterviewQuestionModel evaluateQuestion(Long questionId, EvaluationModel evaluation, Long evaluatorId);

    InterviewQuestionModel findCurrentQuestion(Long interviewId);

    void delete(Long interviewId);

    void setQuestions(InterviewTemplateModel interviewTemplate, InterviewModel startedInterview);
}
