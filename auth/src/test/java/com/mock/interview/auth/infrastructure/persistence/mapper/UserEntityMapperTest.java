package com.mock.interview.auth.infrastructure.persistence.mapper;

import com.mock.interview.auth.factory.UserTestDataFactory;
import com.mock.interview.auth.infrastructure.persistence.entity.UserEntity;
import com.mock.interview.lib.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserEntityMapper Unit Tests")
class UserEntityMapperTest {

  private final UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

  private static Stream<UserEntity> provideUserEntitiesForMapping() {
    return Stream.of(
            UserTestDataFactory.createDefaultUserEntity(),
            UserTestDataFactory.createUserEntity(
                    2L,
                    "inactive@example.com",
                    "hash123",
                    false,
                    LocalDateTime.of(2022, 12, 31, 23, 59),
                    LocalDateTime.of(2023, 1, 1, 0, 1),
                    Set.of()
            ),
            UserTestDataFactory.createUserEntity(
                    null,
                    "no-id@example.com",
                    "hash456",
                    true,
                    null,
                    null,
                    Set.of()
            )
    );
  }

  private static Stream<UserModel> provideUserModelsForMapping() {
    return Stream.of(
            UserTestDataFactory.createDefaultUserModel(),
            UserTestDataFactory.createUserModel(
                    3L,
                    "model@example.com",
                    "modelHash",
                    false,
                    LocalDateTime.of(2021, 1, 1, 0, 0),
                    LocalDateTime.of(2021, 1, 2, 0, 0),
                    Set.of()
            ),
            UserTestDataFactory.createUserModel(
                    null,
                    "no-id-model@example.com",
                    "modelHash2",
                    true,
                    null,
                    null,
                    Set.of()
            )
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
  @MethodSource("provideUserEntitiesForMapping")
  @DisplayName("toModel - with valid entity - returns correct model")
  void toModel_ValidEntity_ReturnsCorrectModel(UserEntity entity) {
    UserModel model = mapper.toModel(entity);

    assertAll(
            () -> assertEquals(entity.getId(), model.getId(), "ID should match"),
            () -> assertEquals(entity.getEmail(), model.getEmail(), "Email should match"),
            () -> assertEquals(entity.getPasswordHash(), model.getPasswordHash(), "Password hash should match"),
            () -> assertEquals(entity.getCreatedAt(), model.getCreatedAt(), "Created date should match"),
            () -> assertEquals(entity.getUpdatedAt(), model.getUpdatedAt(), "Updated date should match")
    );
  }

  @ParameterizedTest
  @MethodSource("provideUserModelsForMapping")
  @DisplayName("toEntity - with valid model - returns correct entity")
  void toEntity_ValidModel_ReturnsCorrectEntity(UserModel model) {
    UserEntity entity = mapper.toEntity(model);

    assertAll(
            () -> assertEquals(model.getId(), entity.getId(), "ID should match"),
            () -> assertEquals(model.getEmail(), entity.getEmail(), "Email should match"),
            () -> assertEquals(model.getPasswordHash(), entity.getPasswordHash(), "Password hash should match"),
            () -> assertEquals(model.getCreatedAt(), entity.getCreatedAt(), "Created date should match"),
            () -> assertEquals(model.getUpdatedAt(), entity.getUpdatedAt(), "Updated date should match"),
            () -> assertNotNull(entity.getOauthProviders(), "OAuth providers should be initialized"),
            () -> assertTrue(entity.getOauthProviders().isEmpty(), "OAuth providers should be empty"),
            () -> assertNotNull(entity.getRoles(), "Roles should be initialized"),
            () -> assertTrue(entity.getRoles().isEmpty(), "Roles should be empty")
    );
  }

  @Test
  @DisplayName("toModels - with list of entities - returns list of models")
  void toModels_ListOfEntities_ReturnsListOfModels() {
    List<UserEntity> entities = List.of(
            UserTestDataFactory.createDefaultUserEntity(),
            UserTestDataFactory.createUserEntity(
                    2L,
                    "user2@example.com",
                    "hash2",
                    false,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    Set.of()
            )
    );

    List<UserModel> models = mapper.toModels(entities);

    assertAll(
            () -> assertEquals(entities.size(), models.size(), "List size should match"),
            () -> assertEquals(entities.getFirst().getEmail(), models.getFirst().getEmail(), "First email should match"),
            () -> assertEquals(entities.get(1).getId(), models.get(1).getId(), "Second ID should match")
    );
  }
}