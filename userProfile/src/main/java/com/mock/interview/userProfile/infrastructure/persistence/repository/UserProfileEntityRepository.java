package com.mock.interview.userProfile.infrastructure.persistence.repository;

import com.mock.interview.userProfile.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileEntityRepository extends JpaRepository<UserProfileEntity, Long> {
}
