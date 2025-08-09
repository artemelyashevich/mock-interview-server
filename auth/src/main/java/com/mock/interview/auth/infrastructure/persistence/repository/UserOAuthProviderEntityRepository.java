package com.mock.interview.auth.infrastructure.persistence.repository;

import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOAuthProviderEntityRepository extends JpaRepository<UserOAuthProviderEntity, Long> {
}
