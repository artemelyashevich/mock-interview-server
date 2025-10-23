package com.mock.interview.lib.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {

    GITHUB("github"),
    GOOGLE("google");

    private final String value;
}
