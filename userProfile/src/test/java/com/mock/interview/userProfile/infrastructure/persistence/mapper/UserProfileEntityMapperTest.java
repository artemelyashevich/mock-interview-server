package com.mock.interview.userProfile.infrastructure.persistence.mapper;

import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.userProfile.infrastructure.persistence.entity.UserProfileEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserProfileEntityMapper Unit Tests")
class UserProfileEntityMapperTest {

    private final UserProfileEntityMapper mapper = Mappers.getMapper(UserProfileEntityMapper.class);

    // Test data
    private static final Long TEST_ID = 1L;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_AVATAR_URL = "https://example.com/avatar.jpg";
    private static final LocalDateTime TEST_LAST_ACTIVITY = LocalDateTime.now().minusHours(1);
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.now().minusHours(2);
    private static final Long TEST_VERSION = 1L;

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
    @MethodSource("provideProfileEntitiesForMapping")
    @DisplayName("toModel - with valid entity - returns correct model")
    void toModel_ValidEntity_ReturnsCorrectModel(UserProfileEntity entity) {
        UserProfileModel model = mapper.toModel(entity);

        assertAll(
                () -> assertEquals(entity.getId(), model.getId(), "ID should match"),
                () -> assertEquals(entity.getFirstName(), model.getFirstName(), "First name should match"),
                () -> assertEquals(entity.getLastName(), model.getLastName(), "Last name should match"),
                () -> assertEquals(entity.getAvatarUrl(), model.getAvatarUrl(), "Avatar URL should match"),
                () -> assertEquals(entity.getLastActivity(), model.getLastActivity(), "Last activity should match"),
                () -> assertEquals(entity.getCreatedAt(), model.getCreatedAt(), "Created date should match"),
                () -> assertEquals(entity.getUpdatedAt(), model.getUpdatedAt(), "Updated date should match")
        );
    }

    @ParameterizedTest
    @MethodSource("provideProfileModelsForMapping")
    @DisplayName("toEntity - with valid model - returns correct entity")
    void toEntity_ValidModel_ReturnsCorrectEntity(UserProfileModel model) {
        UserProfileEntity entity = mapper.toEntity(model);

        assertAll(
                () -> assertEquals(model.getId(), entity.getId(), "ID should match"),
                () -> assertEquals(model.getFirstName(), entity.getFirstName(), "First name should match"),
                () -> assertEquals(model.getLastName(), entity.getLastName(), "Last name should match"),
                () -> assertEquals(model.getAvatarUrl(), entity.getAvatarUrl(), "Avatar URL should match"),
                () -> assertEquals(model.getLastActivity(), entity.getLastActivity(), "Last activity should match"),
                () -> assertEquals(model.getCreatedAt(), entity.getCreatedAt(), "Created date should match"),
                () -> assertEquals(model.getUpdatedAt(), entity.getUpdatedAt(), "Updated date should match")
        );
    }

    @Test
    @DisplayName("toModels - with list of entities - returns list of models")
    void toModels_ListOfEntities_ReturnsListOfModels() {
        List<UserProfileEntity> entities = List.of(
                createTestEntity(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME),
                createTestEntity(2L, "Jane", "Smith")
        );

        List<UserProfileModel> models = mapper.toModels(entities);

        assertAll(
                () -> assertEquals(entities.size(), models.size(), "List size should match"),
                () -> assertEquals(entities.get(0).getFirstName(), models.get(0).getFirstName(), "First name should match"),
                () -> assertEquals(entities.get(1).getLastName(), models.get(1).getLastName(), "Last name should match")
        );
    }

    @Test
    @DisplayName("UserProfileModel - getFullName - returns correct full name")
    void userProfileModel_GetFullName_ReturnsCorrectFullName() {
        UserProfileModel model = createTestModel(TEST_FIRST_NAME, TEST_LAST_NAME);
        assertEquals("John Doe", model.getFullName());
    }

    @Test
    @DisplayName("UserProfileModel - markAsActive - updates last activity timestamp")
    void userProfileModel_MarkAsActive_UpdatesLastActivity() {
        UserProfileModel model = createTestModel(TEST_FIRST_NAME, TEST_LAST_NAME);
        LocalDateTime originalLastActivity = model.getLastActivity();

        model.markAsActive();

        assertAll(
                () -> assertNotEquals(originalLastActivity, model.getLastActivity()),
                () -> assertTrue(model.getLastActivity().isAfter(originalLastActivity))
        );
    }

    private static Stream<UserProfileEntity> provideProfileEntitiesForMapping() {
        return Stream.of(
                createTestEntity(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME),
                createTestEntity(2L, "Jane", "Smith"),
                createTestEntity(null, null, null)
        );
    }

    private static Stream<UserProfileModel> provideProfileModelsForMapping() {
        return Stream.of(
                createTestModel(TEST_FIRST_NAME, TEST_LAST_NAME),
                createTestModel("Alice", "Johnson"),
                createTestModel(null, null)
        );
    }

    private static UserProfileEntity createTestEntity(Long id, String firstName, String lastName) {
        return UserProfileEntity.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .avatarUrl(TEST_AVATAR_URL)
                .lastActivity(TEST_LAST_ACTIVITY)
                .createdAt(TEST_CREATED_AT)
                .updatedAt(TEST_UPDATED_AT)
                .version(TEST_VERSION)
                .build();
    }

    private static UserProfileModel createTestModel(String firstName, String lastName) {
        return UserProfileModel.builder()
                .id(TEST_ID)
                .firstName(firstName)
                .lastName(lastName)
                .avatarUrl(TEST_AVATAR_URL)
                .lastActivity(TEST_LAST_ACTIVITY)
                .createdAt(TEST_CREATED_AT)
                .updatedAt(TEST_UPDATED_AT)
                .build();
    }
}