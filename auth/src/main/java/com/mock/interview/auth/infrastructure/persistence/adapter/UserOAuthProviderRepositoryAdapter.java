package com.mock.interview.auth.infrastructure.persistence.adapter;

import com.mock.interview.auth.application.port.out.UserOAuthProviderRepository;
import com.mock.interview.auth.infrastructure.persistence.mapper.UserOAuthProviderEntityMapper;
import com.mock.interview.auth.infrastructure.persistence.repository.UserOAuthProviderEntityRepository;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserOAuthProviderRepositoryAdapter implements UserOAuthProviderRepository {

    private static final UserOAuthProviderEntityMapper userOauthProviderEntityMapper = UserOAuthProviderEntityMapper.INSTANCE;

    private final UserOAuthProviderEntityRepository userOAuthProviderRepository;

    @Override
    public Optional<UserOAuthProviderModel> findByUserLoginAndProviderId(String userLogin, String providerId) {
        return userOAuthProviderRepository.findByUserLoginAndProviderId(userLogin, providerId);
    }

    @Override
    public UserOAuthProviderModel save(UserOAuthProviderModel userOAuthProviderModel) {
        return userOauthProviderEntityMapper.toModel(
                userOAuthProviderRepository.save(
                        userOauthProviderEntityMapper.toEntity(
                                userOAuthProviderModel
                        )
                )
        );
    }
}
