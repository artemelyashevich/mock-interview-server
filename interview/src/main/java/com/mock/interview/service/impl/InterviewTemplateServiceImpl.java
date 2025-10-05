package com.mock.interview.service.impl;

import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.mapper.InterviewTemplateEntityMapper;
import com.mock.interview.repository.InterviewTemplateRepository;
import com.mock.interview.service.InterviewTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewTemplateServiceImpl implements InterviewTemplateService {

    private static final InterviewTemplateEntityMapper mapper = InterviewTemplateEntityMapper.INSTANCE;

    private final InterviewTemplateRepository interviewTemplateRepository;

    @Override
    @Caching(
            put = {
                    @CachePut(value = "InterviewTemplateService::clone", key = "#interviewTemplate.id()"),
                    @CachePut(value = "InterviewTemplateService::findById", key = "#interviewTemplate.id()")
            }
    )
    @Transactional
    public InterviewTemplateModel create(InterviewTemplateModel interviewTemplateModel) {
        log.debug("Attempting create new interview template");

        if (interviewTemplateRepository.exists(interviewTemplateModel.getTitle(), interviewTemplateModel.getTechnologyStack())) {
            throw new MockInterviewException("Such template already exists", 400);
        }

        var interviewTemplate = interviewTemplateRepository.save(mapper.toEntity(interviewTemplateModel));

        log.debug("Created new interview template");
        return mapper.toModel(interviewTemplate);
    }

    @Override
    public List<InterviewTemplateModel> find(InterviewTemplateModel interviewTemplateModel) {
        log.debug("Attempting find interview template");

        return mapper.toModels(
                interviewTemplateRepository.find(
                        interviewTemplateModel.getTitle(), interviewTemplateModel.getTechnologyStack()
                )
        );
    }

    @Override
    @Cacheable(value = "InterviewTemplateService::findById", key = "#templateId")
    public InterviewTemplateModel findById(Long templateId) {
        return null;
    }

    @Override
    public void runTemplate(Runnable runnable) {
        callAsTemplate(() -> {
            runnable.run();
            return null;
        });
    }

    private <T> T callAsTemplate(Supplier<T> supplier) {
        return supplier.get();
    }
}