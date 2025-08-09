package com.mock.interview.auth.factory;

import com.mock.interview.auth.infrastructure.persistence.entity.Provider;
import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import com.mock.interview.lib.model.UserOAuthProviderModel;

import java.time.LocalDateTime;

public class UserOAuthTestDataFactory {

    public static UserOAuthProviderEntity createOAuthEntity(Long id, String provider, String providerId) {
        return UserOAuthProviderEntity.builder()
                .id(id)
                .provider(Provider.valueOf(provider.toUpperCase()))
                .providerId(providerId)
                .createdAt(LocalDateTime.now())
                .user(UserTestDataFactory.createDefaultUserEntity())
                .build();
    }

    public static UserOAuthProviderModel createOAuthModel(Long id, String provider, String providerId) {
        return UserOAuthProviderModel.builder()
                .id(id)
                .provider(Provider.valueOf(provider.toUpperCase()).getValue())
                .providerId(providerId)
                .createdAt(LocalDateTime.now())
                .user(UserTestDataFactory.createDefaultUserModel())
                .build();
    }

    public static UserOAuthProviderEntity createDefaultOAuthEntity() {
        return createOAuthEntity(1L, "GOOGLE", "google123");
    }

    public static UserOAuthProviderModel createDefaultOAuthModel() {
        return createOAuthModel(1L, "GOOGLE", "google123");
    }
}