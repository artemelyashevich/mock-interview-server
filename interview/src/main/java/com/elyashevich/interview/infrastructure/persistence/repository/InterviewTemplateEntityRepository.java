package com.elyashevich.interview.infrastructure.persistence.repository;

import com.elyashevich.interview.infrastructure.persistence.entity.InterviewTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewTemplateEntityRepository extends JpaRepository<InterviewTemplateEntity, Long> {
    List<InterviewTemplateEntity> findAllByDifficulty(String difficulty);

    List<InterviewTemplateEntity> findAllByTechnologyStackContaining(String technology);

    List<InterviewTemplateEntity> findAllByDifficultyAndTechnologyStackContaining(
        String difficulty,
        String technology
    );
}
