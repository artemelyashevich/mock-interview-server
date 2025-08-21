package com.elyashevich.interview.application.port.in;

import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;

public interface InterviewTemplateService {

    InterviewTemplateModel create(InterviewTemplateModel interviewTemplateModel);

    InterviewQuestionModel clone(Long id, String newTitle);

    InterviewTemplateModel findById(Long templateId);
}
