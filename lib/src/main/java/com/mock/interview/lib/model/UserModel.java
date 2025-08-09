package com.mock.interview.lib.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@With
@Getter
@Setter
@Builder
@ToString(exclude = {
        "passwordHash",
        "roles"
})
public class UserModel implements Serializable {
  private Long id;

  private String email;

  private String passwordHash;

  @Builder.Default
  private boolean isActive = false;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  private LocalDateTime updatedAt = LocalDateTime.now();

  @Builder.Default
  private Set<RoleModel> roles = new HashSet<>();

  public boolean hasRole(String roleName) {
    return roles.stream().anyMatch(role -> role.getName().equals(roleName));
  }
}
