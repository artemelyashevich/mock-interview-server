package com.mock.interview.auth.infrastructure.persistence.adapter;

import com.mock.interview.auth.factory.RoleTestDataFactory;
import com.mock.interview.auth.infrastructure.persistence.mapper.RoleEntityMapper;
import com.mock.interview.auth.infrastructure.persistence.repository.RoleEntityRepository;
import com.mock.interview.lib.model.RoleModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
