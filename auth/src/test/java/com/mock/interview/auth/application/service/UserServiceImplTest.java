package com.mock.interview.auth.application.service;


import com.mock.interview.auth.application.port.out.UserRepository;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.UserModel;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // Test data
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_LOGIN = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final UserModel TEST_USER = UserModel.builder()
            .id(TEST_USER_ID)
            .login(TEST_LOGIN)
            .email(TEST_EMAIL)
            .build();

    @Test
    @DisplayName("findById - existing user - returns user")
    void findById_ExistingUser_ReturnsUser() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID))
                .thenReturn(TEST_USER);

        // Act
        UserModel result = userService.findById(TEST_USER_ID);

        // Assert
        assertEquals(TEST_USER, result);
        verify(userRepository).findById(TEST_USER_ID);
    }

    @Test
    @DisplayName("findByLogin - existing user - returns user")
    void findByLogin_ExistingUser_ReturnsUser() {
        // Arrange
        when(userRepository.findByLogin(TEST_LOGIN))
                .thenReturn(TEST_USER);

        // Act
        UserModel result = userService.findByLogin(TEST_LOGIN);

        // Assert
        assertEquals(TEST_USER, result);
        verify(userRepository).findByLogin(TEST_LOGIN);
    }

    @Test
    @DisplayName("save - existing login - throws ResourceAlreadyExistException")
    void save_ExistingLogin_ThrowsException() {
        // Arrange
        when(userRepository.existsByLogin(TEST_LOGIN))
                .thenReturn(true);

        // Act & Assert
        ResourceAlreadyExistException exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> userService.save(TEST_USER)
        );
        assertEquals(
                String.format("User with login %s already exists", TEST_LOGIN),
                exception.getMessage()
        );
        verify(userRepository).existsByLogin(TEST_LOGIN);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete - non-existent user - throws ResourceNotFoundException")
    void delete_NonExistentUser_ThrowsException() {
        // Arrange
        when(userRepository.existsByLogin(TEST_LOGIN))
                .thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(TEST_USER)
        );
        assertEquals(
                String.format("User with login %s does not exist", TEST_LOGIN),
                exception.getMessage()
        );
        verify(userRepository).existsByLogin(TEST_LOGIN);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("update - existing user - updates and returns user")
    @Transactional
    void update_ExistingUser_UpdatesAndReturnsUser() {
        // Arrange
        UserModel updatedUser = UserModel.builder()
                .id(TEST_USER_ID)
                .login("newlogin")
                .email("new@example.com")
                .oAuthProviderModelList(List.of(new UserOAuthProviderModel()))
                .build();

        when(userRepository.findById(TEST_USER_ID))
                .thenReturn(TEST_USER);
        when(userRepository.save(any(UserModel.class)))
                .thenReturn(updatedUser);

        // Act
        UserModel result = userService.update(updatedUser);

        // Assert
        assertAll(
                () -> assertEquals(updatedUser.getLogin(), result.getLogin()),
                () -> assertEquals(updatedUser.getEmail(), result.getEmail()),
                () -> assertEquals(updatedUser.getOAuthProviderModelList(), result.getOAuthProviderModelList())
        );
        verify(userRepository).findById(TEST_USER_ID);
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    @DisplayName("update - has transactional annotation")
    void update_HasTransactionalAnnotation() throws NoSuchMethodException {
        var method = UserServiceImpl.class.getMethod("update", UserModel.class);
        var transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional);
    }
}