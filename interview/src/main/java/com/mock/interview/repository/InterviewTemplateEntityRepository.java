package com.mock.interview.repository;

import com.mock.interview.entity.InterviewTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewTemplateEntityRepository extends JpaRepository<InterviewTemplateEntity, Long> {
    List<InterviewTemplateEntity> findAllByDifficulty(String difficulty);
}
