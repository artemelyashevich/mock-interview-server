package com.mock.interview.service.impl;

import com.mock.interview.lib.model.InterviewQuestionModel;
import com.mock.interview.lib.model.InterviewTemplateModel;
import com.mock.interview.repository.InterviewTemplateEntityRepository;
import com.mock.interview.service.InterviewTemplateService;
import com.mock.interview.service.mapper.InterviewTemplateMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewTemplateServiceImpl implements InterviewTemplateService {

    private static final InterviewTemplateMapper mapper = Mappers.getMapper(InterviewTemplateMapper.class);

    private final InterviewTemplateEntityRepository interviewTemplateRepository;

    @Override
    @Caching(
            put = {
                    @CachePut(value = "InterviewTemplateService::clone", key = "#interviewTemplate.id()"),
                    @CachePut(value = "InterviewTemplateService::findById", key = "#interviewTemplate.id()")
            }
    )
    public InterviewTemplateModel create(InterviewTemplateModel interviewTemplateModel) {
        log.debug("Attempting create new interview template");

        var interviewTemplate = interviewTemplateRepository.save(mapper.toEntity(interviewTemplateModel));

        log.debug("Created new interview template");
        return mapper.toModel(interviewTemplate);
    }

    @Override
    @Cacheable(value = "InterviewTemplateService::clone", key = "#id")
    public InterviewQuestionModel clone(Long id, String newTitle) {
        return null;
    }

    @Override
    @Cacheable(value = "InterviewTemplateService::findById", key = "#templateId")
    public InterviewTemplateModel findById(Long templateId) {
        var template = interviewTemplateRepository.findById(templateId).orElseThrow(
                () -> {
                    var message = "Interview template with id: '%s' not found'".formatted(templateId);
                    log.error(message);
                    return new EntityNotFoundException(message);
                }
        );
        return mapper.toModel(template);
    }
}