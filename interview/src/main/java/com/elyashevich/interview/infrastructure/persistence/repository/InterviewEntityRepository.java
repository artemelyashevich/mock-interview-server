package com.elyashevich.interview.infrastructure.persistence.repository;

import com.elyashevich.interview.infrastructure.persistence.entity.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewEntityRepository extends JpaRepository<InterviewEntity, Long> {
}
