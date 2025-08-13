package com.mock.interview.auth.application.port.out;

import com.mock.interview.lib.model.UserOAuthProviderModel;

import java.util.Optional;

public interface UserOAuthProviderRepository {
    Optional<UserOAuthProviderModel> findByUserLoginAndProviderId(String userLogin, String providerId);

    UserOAuthProviderModel save(UserOAuthProviderModel userOAuthProviderModel);
}
