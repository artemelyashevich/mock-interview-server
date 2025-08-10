package com.mock.interview.lib.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileModel {
    
    @EqualsAndHashCode.Include
    private final Long id;
    
    private final String firstName;

    private final String lastName;

    private final String avatarUrl;
    
    @Setter
    @Builder.Default
    private LocalDateTime lastActivity = LocalDateTime.now();
    
    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();
    
    @Setter
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    public void markAsActive() {
        this.lastActivity = LocalDateTime.now();
    }
}