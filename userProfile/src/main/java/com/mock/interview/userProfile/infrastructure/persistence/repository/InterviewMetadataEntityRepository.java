package com.mock.interview.userProfile.infrastructure.persistence.repository;

import com.mock.interview.userProfile.infrastructure.persistence.entity.InterviewMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewMetadataEntityRepository extends JpaRepository<InterviewMetadataEntity, Long> {
    List<InterviewMetadataEntity> findAllByUserId(Long userId);

}
