package com.mock.interview.repository;

import com.mock.interview.entity.InterviewTemplateEntity;
import com.mock.interview.lib.specification.GenericSpecificationRepository;

import java.util.List;

public interface InterviewTemplateRepository extends GenericSpecificationRepository<InterviewTemplateEntity, Long> {
    List<InterviewTemplateEntity> findAllByDifficulty(String difficulty);
}
