package com.mock.interview.lib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.Serializable;

@With
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel implements Serializable {
  private Long id;

  private String name;
}
