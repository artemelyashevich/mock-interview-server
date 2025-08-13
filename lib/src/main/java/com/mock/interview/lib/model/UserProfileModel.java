package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.time.LocalDateTime;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileModel extends AbstractModel {

    @EqualsAndHashCode.Include
    private Long id;

    private String firstName;

    private String lastName;

    private Long userId;

    private String avatarUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    @Builder.Default
    private LocalDateTime lastActivity = LocalDateTime.now();

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
