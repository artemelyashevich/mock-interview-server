package com.mock.interview.service.interview;

import com.mock.interview.lib.model.InterviewModel;
import com.mock.interview.lib.model.InterviewTemplateModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface InterviewService {

    InterviewModel create(InterviewModel interviewModel);

    CompletableFuture<List<InterviewModel>> batchSaveAsync(List<InterviewModel> interviews);

    InterviewModel addQuestions(Long interviewId, Long userId);

    InterviewModel startInterview(Long interviewId, Long userId);

    InterviewModel completeInterview(Long interviewId, Long userId);

    InterviewModel findById(Long interviewId);

    InterviewModel findByTemplate(InterviewTemplateModel interviewTemplate);
}
