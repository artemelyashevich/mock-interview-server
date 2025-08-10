package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.RoleService;
import com.mock.interview.auth.application.port.in.SecurityService;
import com.mock.interview.auth.application.port.in.UserService;
import com.mock.interview.auth.application.port.out.UserOAuthProviderRepository;
import com.mock.interview.auth.infrastructure.persistence.entity.OAuthProvider;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.RoleModel;
import com.mock.interview.lib.model.UserModel;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final UserOAuthProviderRepository userOAuthProviderRepository;
    private final RoleService roleService;

    @Override
    public UserModel findCurrentUser() {
        log.debug("Attempting to find Current User");

        var authToken = validateAuthentication(SecurityContextHolder.getContext().getAuthentication());
        var principal = extractPrincipal(authToken);
        var providerId = authToken.getAuthorizedClientRegistrationId();
        var provider = OAuthProvider.valueOf(providerId.toUpperCase());
        String login = principal.getAttribute("email");

        if (OAuthProvider.GITHUB.equals(provider)) {
            login = principal.getAttribute("login");
        }

        var user = userService.findByLogin(login);

        log.debug("Current User is Found");
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public UserModel authenticate(Authentication authentication) {
        log.debug("Attempting to authenticate User via OAuth2");

        var authToken = validateAuthentication(authentication);
        var principal = extractPrincipal(authToken);
        var providerId = authToken.getAuthorizedClientRegistrationId();
        var provider = OAuthProvider.valueOf(providerId.toUpperCase());

        var existingProvider = findExistingProvider(principal, provider);
        if (existingProvider.isPresent()) {
            return handleExistingUser(existingProvider.get(), providerId);
        }

        return createNewUser(principal, provider);
    }

    private OAuth2AuthenticationToken validateAuthentication(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken authToken)) {
            log.debug("Invalid authentication type: {}", authentication.getClass());
            throw new MockInterviewException("Unsupported authentication type", 500);
        }
        return authToken;
    }

    private DefaultOAuth2User extractPrincipal(OAuth2AuthenticationToken authToken) {
        try {
            return (DefaultOAuth2User) authToken.getPrincipal();
        } catch (ClassCastException e) {
            log.debug("Invalid principal type", e);
            throw new MockInterviewException("Invalid user principal", 500);
        }
    }

    private Optional<UserOAuthProviderModel> findExistingProvider(DefaultOAuth2User principal, OAuthProvider provider) {
        String email = principal.getAttribute("email");
        String login = principal.getAttribute("login");

        return switch (provider) {
            case GOOGLE ->
                    userOAuthProviderRepository.findByUserLoginAndProviderId(email, OAuthProvider.GOOGLE.getValue());
            case GITHUB ->
                    userOAuthProviderRepository.findByUserLoginAndProviderId(login, OAuthProvider.GITHUB.getValue());
            default -> Optional.empty();
        };
    }

    private UserModel handleExistingUser(UserOAuthProviderModel existingProvider, String currentProviderId) {
        UserModel user = userService.findByLogin(existingProvider.getLogin());

        if (shouldLinkNewProvider(user, currentProviderId)) {
            linkNewProvider(user, currentProviderId);
        }

        return user;
    }

    private boolean shouldLinkNewProvider(UserModel user, String currentProviderId) {
        return user.getOAuthProviderModelList().size() == 1 &&
                !user.getOAuthProviderModelList().getFirst().getProvider().equalsIgnoreCase(currentProviderId);
    }

    private void linkNewProvider(UserModel user, String currentProviderId) {
        var newProviderType = currentProviderId.equalsIgnoreCase(OAuthProvider.GOOGLE.getValue())
                ? OAuthProvider.GITHUB
                : OAuthProvider.GOOGLE;

        var newProvider = UserOAuthProviderModel.builder()
                .login(
                        newProviderType.getValue().equalsIgnoreCase(OAuthProvider.GITHUB.getValue()) ?
                                user.getLogin() : user.getEmail())
                .provider(newProviderType.getValue().toUpperCase())
                .build();

        var savedProvider = userOAuthProviderRepository.save(newProvider);
        user.addOAuthProviderModel(savedProvider);
        userService.update(user);
    }

    private UserModel createNewUser(DefaultOAuth2User principal, OAuthProvider provider) {
        var userBuilder = UserModel.builder();
        var providerBuilder = UserOAuthProviderModel.builder();

        switch (provider) {
            case GITHUB -> {
                String login = principal.getAttribute("login");
                userBuilder.login(login).emailVerified(false);
                providerBuilder.provider(OAuthProvider.GITHUB.getValue().toUpperCase());
            }
            case GOOGLE -> {
                String email = principal.getAttribute("email");
                userBuilder.login(email).email(email);
                providerBuilder.provider(OAuthProvider.GOOGLE.getValue().toUpperCase());
            }
            default -> throw new MockInterviewException("Unsupported OAuth provider: " + provider, 500);
        }

        var newUser = configureNewUser(userBuilder);
        var newProvider = providerBuilder.login(newUser.getLogin()).build();

        var savedProvider = userOAuthProviderRepository.save(newProvider);
        newUser.addOAuthProviderModel(savedProvider);

        return userService.save(newUser);
    }

    private UserModel configureNewUser(UserModel.UserModelBuilder builder) {
        var defaultRole = roleService.findByName("user");

        return builder
                .roles(Set.of(
                        RoleModel.builder()
                                .id(defaultRole.getId())
                                .name(defaultRole.getName())
                                .build()
                ))
                .isActive(false)
                .build();
    }
}
