package com.mock.interview.userProfile.infrastructure.persistence.mapper;

import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.lib.model.InterviewPosition;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.userProfile.infrastructure.persistence.entity.InterviewMetadataEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InterviewMetadataEntityMapper Unit Tests")
class InterviewMetadataEntityMapperTest {

    private final InterviewMetadataEntityMapper mapper = Mappers.getMapper(InterviewMetadataEntityMapper.class);

    // Test data
    private static final Long TEST_ID = 1L;
    private static final Long TEST_INTERVIEW_ID = 10L;
    private static final Long TEST_USER_ID = 100L;
    private static final LocalDateTime TEST_START_TIME = LocalDateTime.now();
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.now().minusHours(1);

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
    @MethodSource("provideMetadataEntitiesForMapping")
    @DisplayName("toModel - with valid entity - returns correct model")
    void toModel_ValidEntity_ReturnsCorrectModel(InterviewMetadataEntity entity) {
        InterviewMetadataModel model = mapper.toModel(entity);

        assertAll(
                () -> assertEquals(entity.getId(), model.getId(), "ID should match"),
                () -> assertEquals(entity.getInterviewId(), model.getInterviewId(), "Interview ID should match"),
                () -> assertEquals(entity.getUserId(), model.getUserId(), "User ID should match"),
                () -> assertEquals(entity.getPosition(), model.getPosition(), "Position should match"),
                () -> assertEquals(entity.getStatus(), model.getStatus(), "Status should match"),
                () -> assertEquals(entity.getStartTime(), model.getStartTime(), "Start time should match"),
                () -> assertEquals(entity.getCreatedAt(), model.getCreatedAt(), "Created date should match"),
                () -> assertEquals(entity.getUpdatedAt(), model.getUpdatedAt(), "Updated date should match")
        );
    }

    @ParameterizedTest
    @MethodSource("provideMetadataModelsForMapping")
    @DisplayName("toEntity - with valid model - returns correct entity")
    void toEntity_ValidModel_ReturnsCorrectEntity(InterviewMetadataModel model) {
        InterviewMetadataEntity entity = mapper.toEntity(model);

        assertAll(
                () -> assertEquals(model.getId(), entity.getId(), "ID should match"),
                () -> assertEquals(model.getInterviewId(), entity.getInterviewId(), "Interview ID should match"),
                () -> assertEquals(model.getUserId(), entity.getUserId(), "User ID should match"),
                () -> assertEquals(model.getPosition(), entity.getPosition(), "Position should match"),
                () -> assertEquals(model.getStatus(), entity.getStatus(), "Status should match"),
                () -> assertEquals(model.getStartTime(), entity.getStartTime(), "Start time should match"),
                () -> assertEquals(model.getCreatedAt(), entity.getCreatedAt(), "Created date should match"),
                () -> assertEquals(model.getUpdatedAt(), entity.getUpdatedAt(), "Updated date should match")
        );
    }

    @Test
    @DisplayName("toModels - with list of entities - returns list of models")
    void toModels_ListOfEntities_ReturnsListOfModels() {
        List<InterviewMetadataEntity> entities = List.of(
                createTestEntity(TEST_ID, InterviewPosition.MIDDLE, InterviewStatus.IN_PROGRESS),
                createTestEntity(2L, InterviewPosition.SENIOR, InterviewStatus.COMPLETED)
        );

        List<InterviewMetadataModel> models = mapper.toModels(entities);

        assertAll(
                () -> assertEquals(entities.size(), models.size(), "List size should match"),
                () -> assertEquals(entities.get(0).getPosition(), models.get(0).getPosition(), "First position should match"),
                () -> assertEquals(entities.get(1).getStatus(), models.get(1).getStatus(), "Second status should match")
        );
    }

    @Test
    @DisplayName("InterviewMetadataModel - isCompleted - returns correct status")
    void interviewMetadataModel_IsCompleted_ReturnsCorrectStatus() {
        InterviewMetadataModel completedModel = createTestModel(InterviewStatus.COMPLETED);
        InterviewMetadataModel inProgressModel = createTestModel(InterviewStatus.IN_PROGRESS);

        assertAll(
                () -> assertTrue(completedModel.isCompleted()),
                () -> assertFalse(inProgressModel.isCompleted())
        );
    }

    @Test
    @DisplayName("InterviewMetadataModel - changeStatus - updates status and timestamp")
    void interviewMetadataModel_ChangeStatus_UpdatesStatusAndTimestamp() {
        InterviewMetadataModel model = createTestModel(InterviewStatus.IN_PROGRESS);
        LocalDateTime originalUpdatedAt = model.getUpdatedAt();

        InterviewMetadataModel updatedModel = model.changeStatus(InterviewStatus.COMPLETED);

        assertAll(
                () -> assertEquals(InterviewStatus.COMPLETED, updatedModel.getStatus()),
                () -> assertNotEquals(originalUpdatedAt, updatedModel.getUpdatedAt()),
                () -> assertTrue(updatedModel.getUpdatedAt().isAfter(originalUpdatedAt))
        );
    }

    private static Stream<InterviewMetadataEntity> provideMetadataEntitiesForMapping() {
        return Stream.of(
                createTestEntity(TEST_ID, InterviewPosition.JUNIOR, InterviewStatus.IN_PROGRESS),
                createTestEntity(2L, InterviewPosition.MIDDLE, InterviewStatus.COMPLETED),
                createTestEntity(null, InterviewPosition.SENIOR, InterviewStatus.EVALUATED)
        );
    }

    private static Stream<InterviewMetadataModel> provideMetadataModelsForMapping() {
        return Stream.of(
                createTestModel(InterviewStatus.IN_PROGRESS),
                createTestModel(InterviewStatus.COMPLETED),
                createTestModel(InterviewStatus.EVALUATED)
        );
    }

    private static InterviewMetadataEntity createTestEntity(Long id, InterviewPosition position, InterviewStatus status) {
        return InterviewMetadataEntity.builder()
                .id(id)
                .interviewId(TEST_INTERVIEW_ID)
                .userId(TEST_USER_ID)
                .position(position)
                .status(status)
                .startTime(TEST_START_TIME)
                .createdAt(TEST_CREATED_AT)
                .updatedAt(TEST_UPDATED_AT)
                .build();
    }

    private static InterviewMetadataModel createTestModel(InterviewStatus status) {
        return InterviewMetadataModel.builder()
                .id(TEST_ID)
                .interviewId(TEST_INTERVIEW_ID)
                .userId(TEST_USER_ID)
                .position(InterviewPosition.MIDDLE)
                .status(status)
                .startTime(TEST_START_TIME)
                .createdAt(TEST_CREATED_AT)
                .updatedAt(TEST_UPDATED_AT)
                .build();
    }
}