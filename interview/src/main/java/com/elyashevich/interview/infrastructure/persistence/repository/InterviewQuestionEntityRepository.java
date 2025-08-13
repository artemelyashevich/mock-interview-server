package com.elyashevich.interview.infrastructure.persistence.repository;

import com.elyashevich.interview.infrastructure.persistence.entity.InterviewQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterviewQuestionEntityRepository extends JpaRepository<InterviewQuestionEntity, Long> {
    @Query("select i from InterviewQuestionEntity i where i.interview.id = :id")
    List<InterviewQuestionEntity> findAllByInterviewId(Long id);
}
