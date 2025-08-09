package com.mock.interview.auth.factory;

import com.mock.interview.auth.infrastructure.persistence.entity.OAuthProvider;
import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import com.mock.interview.lib.model.UserOAuthProviderModel;

import java.time.LocalDateTime;

public class UserOAuthTestDataFactory {

    public static UserOAuthProviderEntity createOAuthEntity(Long id, String provider) {
        return UserOAuthProviderEntity.builder()
                .id(id)
                .provider(OAuthProvider.valueOf(provider.toUpperCase()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserOAuthProviderModel createOAuthModel(Long id, String provider) {
        return UserOAuthProviderModel.builder()
                .id(id)
                .provider(OAuthProvider.valueOf(provider.toUpperCase()).getValue())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserOAuthProviderEntity createDefaultOAuthEntity() {
        return createOAuthEntity(1L, "GOOGLE");
    }

    public static UserOAuthProviderModel createDefaultOAuthModel() {
        return createOAuthModel(1L, "GOOGLE");
    }
}