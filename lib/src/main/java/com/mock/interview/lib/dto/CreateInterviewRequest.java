package com.mock.interview.lib.dto;

import com.mock.interview.lib.contract.AbstractDto;
import jakarta.validation.constraints.NotNull;
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

    private LocalDateTime startTime;
}
