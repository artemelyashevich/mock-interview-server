package com.mock.interview.auth.infrastructure.persistence.adapter;

import com.mock.interview.auth.factory.UserTestDataFactory;
import com.mock.interview.auth.infrastructure.persistence.entity.UserEntity;
import com.mock.interview.auth.infrastructure.persistence.mapper.UserEntityMapper;
import com.mock.interview.auth.infrastructure.persistence.repository.UserEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter Unit Tests")
class UserRepositoryAdapterTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    @DisplayName("findById - non-existent user - throws ResourceNotFoundException")
    void findById_NonExistentUser_ThrowsException() {
        // Arrange
        Long userId = 999L;
        when(userEntityRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userRepositoryAdapter.findById(userId)
        );
        assertEquals(String.format("User with id %s not found", userId), exception.getMessage());
        verify(userEntityRepository).findById(userId);
    }


    @Test
    @DisplayName("findByEmail - non-existent user - throws ResourceNotFoundException")
    void findByEmail_NonExistentUser_ThrowsException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userRepositoryAdapter.findByEmail(email)
        );
        assertEquals(String.format("User with email %s not found", email), exception.getMessage());
        verify(userEntityRepository).findByEmail(email);
    }

    @Test
    @DisplayName("delete - valid user - delegates to repository")
    void delete_ValidUser_DelegatesToRepository() {
        // Arrange
        UserModel model = UserTestDataFactory.createDefaultUserModel();
        doNothing().when(userEntityRepository).deleteById(model.getId());

        // Act
        userRepositoryAdapter.delete(model);

        // Assert
        verify(userEntityRepository).deleteById(model.getId());
        verifyNoMoreInteractions(userEntityRepository);
        verifyNoInteractions(userEntityMapper);
    }

    @Test
    @DisplayName("existsByEmail - existing email - returns true")
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        // Arrange
        String email = "exists@example.com";
        when(userEntityRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userRepositoryAdapter.existsByEmail(email);

        // Assert
        assertTrue(result);
        verify(userEntityRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("existsByEmail - non-existent email - returns false")
    void existsByEmail_NonExistentEmail_ReturnsFalse() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userEntityRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userRepositoryAdapter.existsByEmail(email);

        // Assert
        assertFalse(result);
        verify(userEntityRepository).existsByEmail(email);
    }
}