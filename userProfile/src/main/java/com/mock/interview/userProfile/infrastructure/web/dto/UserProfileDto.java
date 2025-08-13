package com.mock.interview.userProfile.infrastructure.web.dto;

import com.mock.interview.lib.contract.AbstractDto;
import jakarta.validation.constraints.NotEmpty;
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
public class UserProfileDto extends AbstractDto {
    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name must be not empty")
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotEmpty(message = "Last name must be not empty")
    private String lastName;

    @NotEmpty(message = "User id is required")
    private Long userId;

    private String avatarUrl;
}
