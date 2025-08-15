package com.elyashevich.interview.application.service;

import com.elyashevich.interview.application.port.in.InterviewTemplateService;
import com.elyashevich.interview.application.port.out.InterviewTemplateRepository;
import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewTemplateServiceImpl implements InterviewTemplateService {

    private final InterviewTemplateRepository interviewTemplateRepository;

    @Override
    public InterviewTemplateModel create(InterviewTemplateModel interviewTemplateModel) {
        return null;
    }

    @Override
    public InterviewQuestionModel clone(Long id, String newTitle) {
        return null;
    }
}
