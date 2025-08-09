package com.mock.interview.lib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class UserOAuthProviderModel implements Serializable {

  private Long id;

  private String provider;

  private String providerId;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  private UserModel user;
}
