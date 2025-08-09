package com.mock.interview.auth.factory;

import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import com.mock.interview.auth.infrastructure.persistence.entity.UserEntity;
import com.mock.interview.lib.model.RoleModel;
import com.mock.interview.lib.model.UserModel;

import java.time.LocalDateTime;
import java.util.Set;

public class UserTestDataFactory {

  public static UserEntity createUserEntity(
          Long id,
          String email,
          String passwordHash,
          boolean isActive,
          LocalDateTime createdAt,
          LocalDateTime updatedAt,
          Set<RoleEntity> roles
  ) {
    return UserEntity.builder()
            .id(id)
            .login(email)
            .passwordHash(passwordHash)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .roles(roles)
            .build();
  }

  public static UserModel createUserModel(
          Long id,
          String email,
          String passwordHash,
          boolean isActive,
          LocalDateTime createdAt,
          LocalDateTime updatedAt,
          Set<RoleModel> roles
  ) {
    return UserModel.builder()
            .id(id)
            .login(email)
            .passwordHash(passwordHash)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .roles(roles)
            .build();
  }

  public static UserEntity createDefaultUserEntity() {
    return createUserEntity(
            1L,
            "test@example.com",
            "hashedPassword123",
            true,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 1, 2, 0, 0),
            Set.of()
    );
  }

  public static UserModel createDefaultUserModel() {
    return createUserModel(
            1L,
            "test@example.com",
            "hashedPassword123",
            true,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 1, 2, 0, 0),
            Set.of()
    );
  }
}