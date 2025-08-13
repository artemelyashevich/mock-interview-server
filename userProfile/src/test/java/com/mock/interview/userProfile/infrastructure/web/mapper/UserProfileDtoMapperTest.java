package com.mock.interview.userProfile.infrastructure.web.mapper;

import com.mock.interview.userProfile.infrastructure.web.dto.UserProfileDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileDtoMapperTest {

    private static final UserProfileDtoMapper mapper = UserProfileDtoMapper.INSTANCE;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("toModel - blank names - maps empty strings correctly")
    void toModel_BlankNames_MapsCorrectly(String blankValue) {
        var dto = UserProfileDto.builder()
            .firstName(blankValue)
            .lastName(blankValue)
            .userId(1L)
            .build();

        var result = mapper.toModel(dto);

        assertAll(
            () -> assertEquals(blankValue, result.getFirstName()),
            () -> assertEquals(blankValue, result.getLastName())
        );
    }

    @Test
    @DisplayName("toModel - with avatar URL - handles different URL formats")
    void toModel_WithAvatarUrl_HandlesDifferentFormats() {
        var urls = Stream.of(
            "http://example.com/avatar.jpg",
            "https://example.com/avatar.png",
            "/relative/path.jpg",
            "justfilename.jpg",
            null,
            ""
        );

        urls.forEach(url -> {
            var dto = UserProfileDto.builder()
                .firstName("Test")
                .lastName("User")
                .userId(1L)
                .avatarUrl(url)
                .build();

            var result = mapper.toModel(dto);
            assertEquals(url, result.getAvatarUrl());
        });
    }

    @Test
    @DisplayName("toModel - timestamp fields - sets current time")
    void toModel_TimestampFields_SetsCurrentTime() {
        var beforeTest = java.time.LocalDateTime.now();
        var dto = UserProfileDto.builder()
            .firstName("John")
            .lastName("Doe")
            .userId(1L)
            .build();

        var result = mapper.toModel(dto);
        var afterTest = java.time.LocalDateTime.now();

        assertAll(
            () -> assertTrue(result.getCreatedAt().isAfter(beforeTest) ||
                result.getCreatedAt().equals(beforeTest)),
            () -> assertTrue(result.getCreatedAt().isBefore(afterTest) ||
                result.getCreatedAt().equals(afterTest)),
            () -> assertTrue(result.getUpdatedAt().isAfter(beforeTest) ||
                result.getUpdatedAt().equals(beforeTest)),
            () -> assertTrue(result.getUpdatedAt().isBefore(afterTest) ||
                result.getUpdatedAt().equals(afterTest)),
            () -> assertTrue(result.getLastActivity().isAfter(beforeTest) ||
                result.getLastActivity().equals(beforeTest)),
            () -> assertTrue(result.getLastActivity().isBefore(afterTest) ||
                result.getLastActivity().equals(afterTest))
        );
    }


    private static Stream<Arguments> providePartialDtoInputs() {
        return Stream.of(
            Arguments.of(new UserProfileDto()), // completely empty
            Arguments.of(UserProfileDto.builder().firstName("Only").build()),
            Arguments.of(UserProfileDto.builder().lastName("Name").build()),
            Arguments.of(UserProfileDto.builder().userId(1L).build())
        );
    }
}
