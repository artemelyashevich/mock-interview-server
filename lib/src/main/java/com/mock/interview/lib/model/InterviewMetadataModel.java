package com.mock.interview.lib.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewMetadataModel {
    
    @EqualsAndHashCode.Include
    private final Long id;
    
    @NonNull
    private final Long interviewId;
    
    @NonNull
    private final Long userId;
    
    @NonNull
    private final InterviewPosition position;
    
    @NonNull
    @Setter
    private InterviewStatus status;
    
    @NonNull
    private final LocalDateTime startTime;
    
    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();
    
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