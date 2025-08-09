package com.mock.interview.auth.infrastructure.persistence.adapter;

import com.mock.interview.auth.factory.RoleTestDataFactory;
import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import com.mock.interview.auth.infrastructure.persistence.mapper.RoleEntityMapper;
import com.mock.interview.auth.infrastructure.persistence.repository.RoleEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.RoleModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleRepositoryAdapter Unit Tests")
class RoleRepositoryAdapterTest {

  @Mock
  private RoleEntityRepository roleEntityRepository;

  @Mock
  private RoleEntityMapper roleEntityMapper;

  @InjectMocks
  private RoleRepositoryAdapter roleRepositoryAdapter;

  @Test
  @DisplayName("findByName - non-existent role - throws ResourceNotFoundException")
  void findByName_NonExistentRole_ThrowsException() {
    // Arrange
    String roleName = "NON_EXISTENT";
    when(roleEntityRepository.findByName(roleName)).thenReturn(Optional.empty());

    // Act & Assert
    ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> roleRepositoryAdapter.findByName(roleName)
    );
    assertEquals(String.format("Role with name %s not found", roleName), exception.getMessage());
    verify(roleEntityRepository).findByName(roleName);
  }

  @Test
  @DisplayName("delete - valid model - delegates to repository")
  void delete_ValidModel_DelegatesToRepository() {
    // Arrange
    RoleModel model = RoleTestDataFactory.createRoleModel(1L, "TO_DELETE");
    doNothing().when(roleEntityRepository).deleteById(model.getId());

    // Act
    roleRepositoryAdapter.delete(model);

    // Assert
    verify(roleEntityRepository).deleteById(model.getId());
    verifyNoMoreInteractions(roleEntityRepository);
    verifyNoInteractions(roleEntityMapper);
  }
}