package com.mock.interview.lib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@With
@Getter
@Setter
@Builder
@ToString(exclude = {
        "passwordHash",
        "roles"
})
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements Serializable {
  private Long id;

  private String login;

  private String email;

  @Builder.Default
  private boolean emailVerified = false;

  private String passwordHash;

  @Builder.Default
  private boolean isActive = false;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  private LocalDateTime updatedAt = LocalDateTime.now();

  @Builder.Default
  private Set<RoleModel> roles = new HashSet<>();

  @Builder.Default
  private List<UserOAuthProviderModel> oAuthProviderModelList = new ArrayList<>();

  public void addOAuthProviderModel(UserOAuthProviderModel oAuthProviderModel) {
    oAuthProviderModelList.add(oAuthProviderModel);
  }

  public boolean hasRole(String roleName) {
    return roles.stream().anyMatch(role -> role.getName().equals(roleName));
  }
}
