package com.mock.interview.userProfile.infrastructure.web.mapper;

import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.lib.model.InterviewPosition;
import com.mock.interview.lib.model.InterviewStatus;
import com.mock.interview.lib.dto.InterviewMetadataDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InterviewMetadataDtoMapperTest {

    private static final InterviewMetadataDtoMapper mapper = InterviewMetadataDtoMapper.INSTANCE;

    @ParameterizedTest
    @MethodSource("provideValidDtoInputs")
    @DisplayName("toModel - valid dto - returns correctly mapped model")
    void toModel_ValidDto_ReturnsCorrectlyMappedModel(
        InterviewMetadataDto dto,
        InterviewMetadataModel expectedModel
    ) {
        var result = mapper.toModel(dto);

        assertAll(
            () -> assertEquals(expectedModel.getInterviewId(), result.getInterviewId()),
            () -> assertEquals(expectedModel.getUserId(), result.getUserId()),
            () -> assertEquals(expectedModel.getStatus(), result.getStatus()),
            () -> assertNotNull(result.getCreatedAt()),
            () -> assertNotNull(result.getUpdatedAt())
        );
    }

    @ParameterizedTest
    @EnumSource(InterviewPosition.class)
    @DisplayName("toModel - all position values - maps correctly")
    void toModel_AllPositionValues_MapsCorrectly(InterviewPosition position) {
        var dto = InterviewMetadataDto.builder()
            .interviewId(1L)
            .userId(1L)
            .position(position)
            .status(InterviewStatus.IN_PROGRESS)
            .build();

        var result = mapper.toModel(dto);
        assertEquals(position, result.getPosition());
    }

    @ParameterizedTest
    @EnumSource(InterviewStatus.class)
    @DisplayName("toModel - all status values - maps correctly")
    void toModel_AllStatusValues_MapsCorrectly(InterviewStatus status) {
        var dto = InterviewMetadataDto.builder()
            .interviewId(1L)
            .userId(1L)
            .position(InterviewPosition.JUNIOR)
            .status(status)
            .build();

        var result = mapper.toModel(dto);
        assertEquals(status, result.getStatus());
    }

    @Test
    @DisplayName("toModel - custom start time - preserves time value")
    void toModel_CustomStartTime_PreservesTimeValue() {
        var customTime = LocalDateTime.now().minusDays(1);
        var dto = InterviewMetadataDto.builder()
            .interviewId(1L)
            .userId(1L)
            .position(InterviewPosition.JUNIOR)
            .status(InterviewStatus.IN_PROGRESS)
            .startTime(customTime)
            .build();

        var result = mapper.toModel(dto);
        assertEquals(customTime, result.getStartTime());
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryIds")
    @DisplayName("toModel - boundary ID values - handles correctly")
    void toModel_BoundaryIdValues_HandlesCorrectly(Long interviewId, Long userId) {
        var dto = InterviewMetadataDto.builder()
            .interviewId(interviewId)
            .userId(userId)
            .position(InterviewPosition.JUNIOR)
            .status(InterviewStatus.IN_PROGRESS)
            .build();

        var result = mapper.toModel(dto);

        assertAll(
            () -> assertEquals(interviewId, result.getInterviewId()),
            () -> assertEquals(userId, result.getUserId())
        );
    }

    private static Stream<Arguments> provideBoundaryIds() {
        return Stream.of(
            Arguments.of(1L, 1L), // minimum valid IDs
            Arguments.of(Long.MAX_VALUE, Long.MAX_VALUE), // max values
            Arguments.of(0L, 0L), // zero (invalid but should still map)
            Arguments.of(-1L, -1L) // negative (invalid but should still map)
        );
    }

    @Test
    @DisplayName("toModel - null enums - throws exception")
    void toModel_NullEnums_ThrowsException() {
        var dto = InterviewMetadataDto.builder()
            .interviewId(1L)
            .userId(1L)
            .position(null)
            .status(null)
            .build();

        assertThrows(NullPointerException.class, () -> mapper.toModel(dto));
    }

    private static Stream<Arguments> provideValidDtoInputs() {
        var now = LocalDateTime.now();
        return Stream.of(
            Arguments.of(
                InterviewMetadataDto.builder()
                    .interviewId(1L)
                    .userId(1L)
                    .position(InterviewPosition.JUNIOR)
                    .status(InterviewStatus.IN_PROGRESS)
                    .startTime(now)
                    .build(),
                InterviewMetadataModel.builder()
                    .interviewId(1L)
                    .userId(1L)
                    .position(InterviewPosition.MIDDLE)
                    .status(InterviewStatus.IN_PROGRESS)
                    .startTime(now)
                    .build()
            ),
            Arguments.of(
                InterviewMetadataDto.builder()
                    .interviewId(2L)
                    .userId(3L)
                    .position(InterviewPosition.SENIOR)
                    .status(InterviewStatus.COMPLETED)
                    .startTime(now.plusHours(1))
                    .build(),
                InterviewMetadataModel.builder()
                    .interviewId(2L)
                    .userId(3L)
                    .position(InterviewPosition.JUNIOR)
                    .status(InterviewStatus.COMPLETED)
                    .startTime(now.plusHours(1))
                    .build()
            )
        );
    }
}
