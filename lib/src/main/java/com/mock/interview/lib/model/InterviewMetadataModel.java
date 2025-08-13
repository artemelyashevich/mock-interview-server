package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.time.LocalDateTime;
import java.util.Objects;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewMetadataModel extends AbstractModel {

    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    private Long interviewId;

    @NonNull
    private Long userId;

    @NonNull
    private InterviewPosition position;

    @NonNull
    private LocalDateTime startTime;

    @NonNull
    @Setter
    private InterviewStatus status;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public boolean isCompleted() {
        return status == InterviewStatus.COMPLETED;
    }

    public InterviewMetadataModel changeStatus(InterviewStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus);
        this.updatedAt = LocalDateTime.now();
        return this;
    }
}
