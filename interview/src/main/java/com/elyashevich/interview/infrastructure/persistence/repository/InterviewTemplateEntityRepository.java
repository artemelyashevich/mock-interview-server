package com.elyashevich.interview.infrastructure.persistence.repository;

import com.elyashevich.interview.infrastructure.persistence.entity.InterviewTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewTemplateEntityRepository extends JpaRepository<InterviewTemplateEntity, Long> {
}
