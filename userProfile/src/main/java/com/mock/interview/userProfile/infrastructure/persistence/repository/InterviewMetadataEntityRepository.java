package com.mock.interview.userProfile.infrastructure.persistence.repository;

import com.mock.interview.userProfile.infrastructure.persistence.entity.InterviewMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewMetadataEntityRepository extends JpaRepository<InterviewMetadataEntity, Long> {
}
