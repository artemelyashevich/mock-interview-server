package com.mock.interview.infrastructure.web.dto;

import com.mock.interview.lib.contract.AbstractDto;
import com.mock.interview.lib.model.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateInterviewRequest extends AbstractDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Template ID is required")
    private Long templateId;

    private InterviewStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
