package com.mock.interview.lib.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RoleModel implements Serializable {
  private Long id;

  private String name;
}
