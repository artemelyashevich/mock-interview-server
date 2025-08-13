package com.mock.interview.userProfile.infrastructure.web.controller;

import com.mock.interview.lib.exception.ExceptionBody;
import com.mock.interview.lib.model.InterviewMetadataModel;
import com.mock.interview.userProfile.application.port.in.InterviewMetadataService;
import com.mock.interview.userProfile.infrastructure.web.dto.InterviewMetadataDto;
import com.mock.interview.userProfile.infrastructure.web.mapper.InterviewMetadataDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
@Tag(name = "Interview Metadata", description = "API for managing interview metadata")
public class InterviewMetadataController {
    private static final InterviewMetadataDtoMapper interviewMetadataDtoMapper = InterviewMetadataDtoMapper.INSTANCE;

    private final InterviewMetadataService interviewMetadataService;

    @Operation(
        summary = "Get all interviews",
        description = "Returns a list of all interview metadata records",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InterviewMetadataModel.class, type = "array"))
            )
        }
    )
    @GetMapping
    public List<InterviewMetadataModel> findAll() {
        return interviewMetadataService.findAll();
    }

    @Operation(
        summary = "Get interview by ID",
        description = "Returns a single interview metadata record by its ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Interview found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InterviewMetadataModel.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Interview not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionBody.class))
            )
        }
    )
    @GetMapping("/{id}")
    public InterviewMetadataModel findById(
        @Parameter(description = "ID of the interview to fetch", required = true, example = "1")
        @PathVariable Long id
    ) {
        return interviewMetadataService.findById(id);
    }

    @Operation(
        summary = "Get interviews by user ID",
        description = "Returns all interviews associated with a given user ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Interviews found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InterviewMetadataModel.class, type = "array"))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Interview not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionBody.class))
            )
        }
    )
    @GetMapping("/user/{userId}")
    public List<InterviewMetadataModel> findByUserId(
        @Parameter(description = "User ID to filter interviews", required = true, example = "123")
        @PathVariable("userId") Long userId
    ) {
        return interviewMetadataService.findByUserId(userId);
    }

    @Operation(
        summary = "Create a new interview",
        description = "Saves a new interview metadata record",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Interview created",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InterviewMetadataModel.class))
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
    public InterviewMetadataModel save(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Interview metadata to create",
            required = true,
            content = @Content(schema = @Schema(implementation = InterviewMetadataDto.class))
        )
        @Validated @RequestBody InterviewMetadataDto interviewMetadataDto
    ) {
        return interviewMetadataService.save(interviewMetadataDtoMapper.toModel(interviewMetadataDto));
    }
}
