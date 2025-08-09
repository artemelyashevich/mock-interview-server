package com.mock.interview.auth.infrastructure.persistence.mapper;

import com.mock.interview.auth.factory.UserOAuthTestDataFactory;
import com.mock.interview.auth.infrastructure.persistence.entity.Provider;
import com.mock.interview.auth.infrastructure.persistence.entity.UserOAuthProviderEntity;
import com.mock.interview.lib.model.UserOAuthProviderModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("UserOAuthProviderMapper Unit Tests")
class UserOAuthProviderMapperTest {

    private final UserOAuthProviderEntityMapper mapper = Mappers.getMapper(UserOAuthProviderEntityMapper.class);

    private static Stream<UserOAuthProviderEntity> provideOAuthEntitiesForMapping() {
        return Stream.of(
                UserOAuthTestDataFactory.createDefaultOAuthEntity(),
                UserOAuthTestDataFactory.createOAuthEntity(2L, Provider.GITHUB.getValue(), "github123"),
                UserOAuthTestDataFactory.createOAuthEntity(3L, Provider.GITHUB.getValue(), "fb123"),
                UserOAuthTestDataFactory.createOAuthEntity(null, Provider.GOOGLE.getValue(), "linkedin123")
        );
    }

    private static Stream<UserOAuthProviderModel> provideOAuthModelsForMapping() {
        return Stream.of(
                UserOAuthTestDataFactory.createDefaultOAuthModel(),
                UserOAuthTestDataFactory.createOAuthModel(2L, Provider.GITHUB.getValue().toUpperCase(), "twitter123"),
                UserOAuthTestDataFactory.createOAuthModel(3L, Provider.GOOGLE.getValue().toUpperCase(), "apple123"),
                UserOAuthTestDataFactory.createOAuthModel(null, Provider.GITHUB.getValue().toUpperCase(), "ms123")
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
    @MethodSource("provideOAuthEntitiesForMapping")
    @DisplayName("toModel - with valid entity - returns correct model")
    void toModel_ValidEntity_ReturnsCorrectModel(UserOAuthProviderEntity entity) {
        UserOAuthProviderModel model = mapper.toModel(entity);

        assertAll(
                () -> assertEquals(entity.getId(), model.getId(), "ID should match"),
                () -> assertEquals(entity.getProvider().name(), model.getProvider(), "Provider should match"),
                () -> assertEquals(entity.getProviderId(), model.getProviderId(), "Provider ID should match"),
                () -> assertEquals(entity.getCreatedAt(), model.getCreatedAt(), "Created date should match")
        );
    }

    @Test
    @DisplayName("toModels - with list of entities - returns list of models")
    void toModels_ListOfEntities_ReturnsListOfModels() {
        var entities = List.of(
                UserOAuthTestDataFactory.createDefaultOAuthEntity(),
                UserOAuthTestDataFactory.createOAuthEntity(2L, "GOOGLE", "google123")
        );

        var models = mapper.toModels(entities);

        assertAll(
                () -> assertEquals(entities.size(), models.size(), "List size should match"),
                () -> assertEquals(entities.getFirst().getProvider().name(), models.getFirst().getProvider(), "First provider should match"),
                () -> assertEquals(entities.get(1).getProviderId(), models.get(1).getProviderId(), "Second provider ID should match")
        );
    }
}