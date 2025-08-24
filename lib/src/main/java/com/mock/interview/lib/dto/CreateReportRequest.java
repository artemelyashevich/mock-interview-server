package com.mock.interview.lib.dto;

import com.mock.interview.lib.contract.AbstractDto;
import com.mock.interview.lib.model.ReportFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequest extends AbstractDto {
        @NotBlank(message = "Title is required")
        private String title;

        private String description;

        @NotNull(message = "Format is required")
        private ReportFormat format;

        @NotBlank(message = "Created by field is required")
        private Long interviewId;
}