package com.mock.interview.lib.dto;

import com.mock.interview.lib.model.ReportFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Format is required")
        ReportFormat format,

        @NotBlank(message = "Created by field is required")
        Long interviewId
) {}