package com.mock.interview.auth.infrastructure.persistence.repository;

import com.mock.interview.auth.infrastructure.persistence.entity.OAuthProvider;
import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOAuthProviderEntityRepository extends JpaRepository<UserOAuthProviderEntity, Long> {

  Optional<UserOAuthProviderEntity> findByLoginAndProvider(String userLogin, OAuthProvider provider);
}
