package com.mock.interview.auth.infrastructure.persistence.repository;

import com.mock.interview.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    @Query("select (count(u) > 0) from UserEntity u where u.login = :login")
    boolean existsByLogin(String login);
}
