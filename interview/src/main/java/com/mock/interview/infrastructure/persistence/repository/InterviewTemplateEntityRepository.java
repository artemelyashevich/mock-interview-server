package com.mock.interview.infrastructure.persistence.repository;

import com.mock.interview.infrastructure.persistence.entity.InterviewTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewTemplateEntityRepository extends JpaRepository<InterviewTemplateEntity, Long> {
    List<InterviewTemplateEntity> findAllByDifficulty(String difficulty);
}
