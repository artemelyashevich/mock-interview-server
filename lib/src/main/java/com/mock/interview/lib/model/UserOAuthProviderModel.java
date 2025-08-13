package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.Serializable;
import java.time.LocalDateTime;

@With
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserOAuthProviderModel extends AbstractModel {

    private Long id;

    private String provider;

    private String providerId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String login;
}
