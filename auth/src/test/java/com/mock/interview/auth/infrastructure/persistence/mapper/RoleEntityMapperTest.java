package com.mock.interview.auth.infrastructure.persistence.mapper;

import com.mock.interview.auth.factory.RoleTestDataFactory;
import com.mock.interview.auth.infrastructure.persistence.entity.RoleEntity;
import com.mock.interview.lib.model.RoleModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RoleEntityMapper Unit Tests")
class RoleEntityMapperTest {

    private final RoleEntityMapper mapper = Mappers.getMapper(RoleEntityMapper.class);

    private static Stream<RoleEntity> provideRoleEntitiesForMapping() {
        return Stream.of(
            RoleTestDataFactory.createDefaultRoleEntity(),
            RoleTestDataFactory.createRoleEntity(2L, "USER"),
            RoleTestDataFactory.createRoleEntity(3L, "GUEST"),
            RoleTestDataFactory.createRoleEntity(null, "NO_ID")
        );
    }

    private static Stream<RoleModel> provideRoleModelsForMapping() {
        return Stream.of(
            RoleTestDataFactory.createDefaultRoleModel(),
            RoleTestDataFactory.createRoleModel(2L, "USER"),
            RoleTestDataFactory.createRoleModel(3L, "GUEST"),
            RoleTestDataFactory.createRoleModel(null, "NO_ID")
        );
    }

    @Test
    @DisplayName("toModel - with null entity - returns null")
    void toModel_NullEntity_ReturnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    @DisplayName("toEntity - with null model - returns null")
    void toEntity_NullModel_ReturnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("toModels - with null entities - returns null")
    void toModels_NullEntities_ReturnsNull() {
        assertNull(mapper.toModels(null));
    }

    @ParameterizedTest
    @MethodSource("provideRoleEntitiesForMapping")
    @DisplayName("toModel - with valid entity - returns correct model")
    void toModel_ValidEntity_ReturnsCorrectModel(RoleEntity entity) {
        RoleModel model = mapper.toModel(entity);

        assertAll(
            () -> assertEquals(entity.getId(), model.getId(), "ID should match"),
            () -> assertEquals(entity.getName(), model.getName(), "Name should match")
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoleModelsForMapping")
    @DisplayName("toEntity - with valid model - returns correct entity")
    void toEntity_ValidModel_ReturnsCorrectEntity(RoleModel model) {
        RoleEntity entity = mapper.toEntity(model);

        assertAll(
            () -> assertEquals(model.getId(), entity.getId(), "ID should match"),
            () -> assertEquals(model.getName(), entity.getName(), "Name should match"),
            () -> assertNotNull(entity.getUsers(), "Users set should be initialized"),
            () -> assertTrue(entity.getUsers().isEmpty(), "Users set should be empty")
        );
    }

    @Test
    @DisplayName("toModels - with list of entities - returns list of models")
    void toModels_ListOfEntities_ReturnsListOfModels() {
        List<RoleEntity> entities = List.of(
            RoleTestDataFactory.createRoleEntity(1L, "ADMIN"),
            RoleTestDataFactory.createRoleEntity(2L, "USER")
        );

        List<RoleModel> models = mapper.toModels(entities);

        assertAll(
            () -> assertEquals(entities.size(), models.size(), "List size should match"),
            () -> assertEquals(entities.get(0).getId(), models.get(0).getId(), "First element ID should match"),
            () -> assertEquals(entities.get(1).getName(), models.get(1).getName(), "Second element name should match")
        );
    }
}
