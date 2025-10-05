package com.mock.interview.service;

import com.mock.interview.lib.model.InterviewTemplateModel;

import java.util.List;

public interface InterviewTemplateService {

    InterviewTemplateModel create(InterviewTemplateModel interviewTemplateModel);

    List<InterviewTemplateModel> find(InterviewTemplateModel interviewTemplateModel);

    InterviewTemplateModel findById(Long templateId);

    void runTemplate(Runnable runnable);
}
