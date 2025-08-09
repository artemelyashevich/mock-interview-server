package com.mock.interview.auth.infrastructure.persistence.repository;

import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserOAuthProviderEntityRepository extends JpaRepository<UserOAuthProviderEntity, Long> {

  @Query("select u from UserOAuthProviderEntity u where u.user.login = :userLogin and u.providerId = :providerId")
  Optional<UserOAuthProviderModel> findByUserLoginAndProviderId(String userLogin, String providerId);
}
