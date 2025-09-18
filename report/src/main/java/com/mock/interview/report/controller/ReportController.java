package com.mock.interview.report.controller;

import com.mock.interview.lib.dto.CreateReportRequest;
import com.mock.interview.lib.model.ReportModel;
import com.mock.interview.report.service.ReportService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "API for managing interview reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Get reports by interview ID",
            description = "Retrieves all reports associated with a specific interview"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved reports",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Interview not found"
            )
    })
    @GetMapping("/interview/{id}")
    public List<ReportModel> findAllByInterviewId(
            @Parameter(description = "ID of the interview", example = "1", required = true)
            @PathVariable Long id) {
        return reportService.findAllByInterviewId(id);
    }

    @Operation(
            summary = "Get report by ID",
            description = "Retrieves a specific report by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved report",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @GetMapping("/{id}")
    public ReportModel findById(
            @Parameter(description = "ID of the report", example = "1", required = true)
            @PathVariable Long id) {
        return reportService.findById(id);
    }

    @Operation(
            summary = "Create a new report",
            description = "Creates a new report for an interview"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created report",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportModel.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PostMapping
    public ReportModel create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Report creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateReportRequest.class))
            )
            @Valid @RequestBody CreateReportRequest createReportRequest) {
        return reportService.create(createReportRequest);
    }

    @Operation(
            summary = "Delete report by ID",
            description = "Deletes a specific report by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted report"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteById(
            @Parameter(description = "ID of the report to delete", example = "1", required = true)
            @PathVariable Long id) {
        reportService.deleteById(id);
    }
}