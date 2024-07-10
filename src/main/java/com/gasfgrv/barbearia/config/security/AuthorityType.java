package com.gasfgrv.barbearia.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorityType {
    CLIENTE("CLIENTE"),
    BARBEIRO("BARBEIRO");

    private final String authority;

}
