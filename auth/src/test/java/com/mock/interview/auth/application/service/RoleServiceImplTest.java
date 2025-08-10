package com.mock.interview.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.mock.interview.auth.application.port.out.RoleRepository;
import com.mock.interview.auth.infrastructure.persistence.mapper.RoleEntityMapper;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.RoleModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleService Unit Tests")
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleEntityMapper roleEntityMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    // Test data
    private static final String TEST_ROLE_NAME = "TEST_ROLE";
    private static final String EXISTING_ROLE_NAME = "EXISTING_ROLE";
    private static final RoleModel TEST_ROLE = RoleModel.builder().name(TEST_ROLE_NAME).build();
    private static final RoleModel EXISTING_ROLE = RoleModel.builder().name(EXISTING_ROLE_NAME).build();

    @Test
    @DisplayName("findAll - returns all roles")
    void findAll_ReturnsAllRoles() {
        // Arrange
        var expectedRoles = List.of(TEST_ROLE, EXISTING_ROLE);
        when(roleRepository.findAll()).thenReturn(expectedRoles);

        // Act
        List<RoleModel> result = roleService.findAll();

        // Assert
        assertEquals(expectedRoles, result);
        verify(roleRepository).findAll();
    }

    @Test
    @DisplayName("findByName - non-existent role - creates and returns new role")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void findByName_NonExistentRole_CreatesAndReturnsNewRole() {
        // Arrange
        when(roleRepository.findByName(TEST_ROLE_NAME))
                .thenReturn(Optional.empty());
        when(roleRepository.save(any(RoleModel.class)))
                .thenReturn(TEST_ROLE);

        // Act
        RoleModel result = roleService.findByName(TEST_ROLE_NAME);

        // Assert
        assertEquals(TEST_ROLE_NAME, result.getName());
        verify(roleRepository).findByName(TEST_ROLE_NAME);
        verify(roleRepository).save(any(RoleModel.class));
    }

    @Test
    @DisplayName("save - new role - saves and returns role")
    @Transactional
    void save_NewRole_SavesAndReturnsRole() {
        // Arrange
        when(roleRepository.existsByName(TEST_ROLE_NAME))
                .thenReturn(false);
        when(roleRepository.save(TEST_ROLE))
                .thenReturn(TEST_ROLE);

        // Act
        RoleModel result = roleService.save(TEST_ROLE);

        // Assert
        assertEquals(TEST_ROLE, result);
        verify(roleRepository).existsByName(TEST_ROLE_NAME);
        verify(roleRepository).save(TEST_ROLE);
    }

    @Test
    @DisplayName("save - existing role - throws ResourceAlreadyExistException")
    @Transactional
    void save_ExistingRole_ThrowsException() {
        // Arrange
        when(roleRepository.existsByName(EXISTING_ROLE_NAME))
                .thenReturn(true);

        // Act & Assert
        ResourceAlreadyExistException exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> roleService.save(EXISTING_ROLE)
        );
        assertEquals(
                String.format("Role with name %s already exists", EXISTING_ROLE_NAME),
                exception.getMessage()
        );
        verify(roleRepository).existsByName(EXISTING_ROLE_NAME);
        verify(roleRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete - existing role - deletes role")
    void delete_ExistingRole_DeletesRole() {
        // Arrange
        when(roleRepository.existsByName(EXISTING_ROLE_NAME))
                .thenReturn(true);
        doNothing().when(roleRepository).delete(EXISTING_ROLE);

        // Act
        roleService.delete(EXISTING_ROLE);

        // Assert
        verify(roleRepository).existsByName(EXISTING_ROLE_NAME);
        verify(roleRepository).delete(EXISTING_ROLE);
    }

    @Test
    @DisplayName("delete - non-existent role - throws ResourceNotFoundException")
    void delete_NonExistentRole_ThrowsException() {
        // Arrange
        when(roleRepository.existsByName(TEST_ROLE_NAME))
                .thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> roleService.delete(TEST_ROLE)
        );
        assertEquals(
                String.format("Role with name %s does not exist", TEST_ROLE_NAME),
                exception.getMessage()
        );
        verify(roleRepository).existsByName(TEST_ROLE_NAME);
        verify(roleRepository, never()).delete(any());
    }

    @Test
    @DisplayName("findByName - requires new transaction propagation")
    void findByName_HasRequiresNewTransactionPropagation() throws NoSuchMethodException {
        var method = RoleServiceImpl.class.getMethod("findByName", String.class);
        var transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional);
        assertEquals(Propagation.REQUIRES_NEW, transactional.propagation());
    }

    @Test
    @DisplayName("save - has transactional annotation")
    void save_HasTransactionalAnnotation() throws NoSuchMethodException {
        var method = RoleServiceImpl.class.getMethod("save", RoleModel.class);
        var transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional);
    }
}