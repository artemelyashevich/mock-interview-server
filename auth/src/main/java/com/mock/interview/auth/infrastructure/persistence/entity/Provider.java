package com.mock.interview.auth.infrastructure.persistence.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {

  GITHUB("github"),
  GOOGLE("google");

  private final String value;
}
