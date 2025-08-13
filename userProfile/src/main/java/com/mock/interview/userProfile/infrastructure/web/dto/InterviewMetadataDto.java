package com.mock.interview.userProfile.infrastructure.web.dto;

import com.mock.interview.lib.contract.AbstractDto;
import com.mock.interview.lib.model.InterviewPosition;
import com.mock.interview.lib.model.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InterviewMetadataDto extends AbstractDto {

    @NotNull(message = "Interview id is required")
    private Long interviewId;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Position is required")
    private InterviewPosition position;

    @Builder.Default
    private LocalDateTime startTime = LocalDateTime.now();

    @NotNull(message = "Status is required")
    private InterviewStatus status;
}
