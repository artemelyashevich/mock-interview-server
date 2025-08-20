package com.mock.interview.userProfile.infrastructure.persistence.repository;

import com.mock.interview.lib.specification.GenericSpecificationRepository;
import com.mock.interview.userProfile.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileEntityRepository extends GenericSpecificationRepository<UserProfileEntity, Long> {
}
