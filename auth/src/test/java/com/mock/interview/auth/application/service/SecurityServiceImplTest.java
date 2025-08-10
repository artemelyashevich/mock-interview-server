package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.RoleService;
import com.mock.interview.auth.application.port.in.UserService;
import com.mock.interview.auth.application.port.out.UserOAuthProviderRepository;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.RoleModel;
import com.mock.interview.lib.model.UserModel;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityService Unit Tests")
class SecurityServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserOAuthProviderRepository userOAuthProviderRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private SecurityServiceImpl securityService;

    // Test data
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_LOGIN = "testuser";
    private static final String GOOGLE_PROVIDER = "google";
    private static final String GITHUB_PROVIDER = "github";

    @Test
    @DisplayName("findCurrentUser - Google authentication - returns user by email")
    void findCurrentUser_GoogleAuth_ReturnsUserByEmail() {
        // Arrange
        var principal = createOAuth2User(Map.of("email", TEST_EMAIL));
        var authToken = new OAuth2AuthenticationToken(principal, null, GOOGLE_PROVIDER);
        setSecurityContext(authToken);

        var expectedUser = new UserModel();
        when(userService.findByLogin(TEST_EMAIL)).thenReturn(expectedUser);

        // Act
        UserModel result = securityService.findCurrentUser();

        // Assert
        assertEquals(expectedUser, result);
        verify(userService).findByLogin(TEST_EMAIL);
    }

    @Test
    @DisplayName("authenticate - new Google user - creates user with Google provider")
    void authenticate_NewGoogleUser_CreatesUserWithGoogleProvider() {
        // Arrange
        var principal = createOAuth2User(Map.of("email", TEST_EMAIL));
        var authToken = new OAuth2AuthenticationToken(principal, null, GOOGLE_PROVIDER);

        when(userOAuthProviderRepository.findByUserLoginAndProviderId(TEST_EMAIL, GOOGLE_PROVIDER))
                .thenReturn(Optional.empty());

        var role = new RoleModel();
        role.setName("user");
        when(roleService.findByName("user")).thenReturn(role);

        var savedProvider = new UserOAuthProviderModel();
        when(userOAuthProviderRepository.save(any())).thenReturn(savedProvider);

        var expectedUser = new UserModel();
        when(userService.save(any())).thenReturn(expectedUser);

        // Act
        UserModel result = securityService.authenticate(authToken);

        // Assert
        assertEquals(expectedUser, result);
        verify(userService).save(any());
        verify(userOAuthProviderRepository).save(any());
    }

    @Test
    @DisplayName("authenticate - invalid authentication type - throws exception")
    void authenticate_InvalidAuthenticationType_ThrowsException() {
        // Arrange
        Authentication invalidAuth = mock(Authentication.class);

        // Act & Assert
        assertThrows(MockInterviewException.class, () -> securityService.authenticate(invalidAuth));
    }

    @Test
    @DisplayName("authenticate - invalid principal type - throws exception")
    void authenticate_InvalidPrincipalType_ThrowsException() {
        // Arrange
        OAuth2AuthenticationToken authToken = mock(OAuth2AuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(mock(OAuth2User.class));

        // Act & Assert
        assertThrows(MockInterviewException.class, () -> securityService.authenticate(authToken));
    }

    private DefaultOAuth2User createOAuth2User(Map<String, Object> attributes) {
        return new DefaultOAuth2User(null, attributes, "email");
    }

    private void setSecurityContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}