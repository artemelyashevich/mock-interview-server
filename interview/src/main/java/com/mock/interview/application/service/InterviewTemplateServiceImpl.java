package com.mock.interview.application.service;

import com.mock.interview.application.port.in.InterviewTemplateService;
import com.mock.interview.application.port.out.InterviewTemplateRepository;
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
        log.debug("Attempting create new interview template");

        var interviewTemplate = interviewTemplateRepository.save(interviewTemplateModel);

        log.debug("Created new interview template");
        return interviewTemplate;
    }

    @Override
    public InterviewQuestionModel clone(Long id, String newTitle) {
        return null;
    }

    @Override
    public InterviewTemplateModel findById(Long templateId) {
        return interviewTemplateRepository.findById(templateId);
    }
}
