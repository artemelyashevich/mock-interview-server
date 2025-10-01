package com.mock.interview.service.impl;

import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.repository.InterviewTemplateRepository;
import com.mock.interview.service.InterviewTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewTemplateServiceImpl implements InterviewTemplateService {

    private final InterviewTemplateRepository interviewTemplateRepository;

    @Override
    @Caching(
            put = {
                    @CachePut(value = "InterviewTemplateService::clone", key = "#interviewTemplate.id()"),
                    @CachePut(value = "InterviewTemplateService::findById", key = "#interviewTemplate.id()")
            }
    )
    public InterviewTemplateModel create(InterviewTemplateModel interviewTemplateModel) {
        log.debug("Attempting create new interview template");


        log.debug("Created new interview template");
        return null;
    }

    @Override
    @Cacheable(value = "InterviewTemplateService::clone", key = "#id")
    public InterviewQuestionModel clone(Long id, String newTitle) {
        return null;
    }

    @Override
    @Cacheable(value = "InterviewTemplateService::findById", key = "#templateId")
    public InterviewTemplateModel findById(Long templateId) {
        return null;
    }
}