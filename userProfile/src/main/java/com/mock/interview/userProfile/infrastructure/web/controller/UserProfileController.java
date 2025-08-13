package com.mock.interview.userProfile.infrastructure.web.controller;

import com.mock.interview.lib.exception.ExceptionBody;
import com.mock.interview.lib.model.UserProfileModel;
import com.mock.interview.userProfile.application.port.in.UserProfileService;
import com.mock.interview.userProfile.infrastructure.web.dto.UserProfileDto;
import com.mock.interview.userProfile.infrastructure.web.mapper.UserProfileDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userProfiles")
@RequiredArgsConstructor
@Tag(name = "User Profiles", description = "API for managing user profiles")
public class UserProfileController {

    private static final UserProfileDtoMapper userProfileDtoMapper = UserProfileDtoMapper.INSTANCE;

    private final UserProfileService userProfileService;

    @Operation(
        summary = "Get all user profiles",
        description = "Returns a list of all user profile records",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserProfileModel.class, type = "array"))
            )
        }
    )
    @GetMapping
    public List<UserProfileModel> findAll() {
        return userProfileService.findAll();
    }

    @Operation(
        summary = "Get user profile by ID",
        description = "Returns a single user profile record by its ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User profile found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserProfileModel.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User profile not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionBody.class))
            )
        }
    )
    @GetMapping("/{id}")
    public UserProfileModel findById(
        @Parameter(description = "ID of the user profile to fetch", required = true, example = "1")
        @PathVariable("id") Long id
    ) {
        return userProfileService.findById(id);
    }

    @Operation(
        summary = "Create a new user profile",
        description = "Saves a new user profile record",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User profile created",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserProfileModel.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionBody.class))
            )
        }
    )
    @PostMapping
    public UserProfileModel create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User profile data to create",
            required = true,
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        )
        @Validated @RequestBody UserProfileDto userProfileModel
    ) {
        return userProfileService.save(userProfileDtoMapper.toModel(userProfileModel));
    }

    @Operation(
        summary = "Delete a user profile",
        description = "Deletes a user profile record by its ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User profile deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User profile not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionBody.class))
            )
        }
    )
    @DeleteMapping("/{id}")
    public void delete(
        @Parameter(description = "ID of the user profile to delete", required = true, example = "1")
        @PathVariable("id") Long id
    ) {
        userProfileService.delete(id);
    }
}
