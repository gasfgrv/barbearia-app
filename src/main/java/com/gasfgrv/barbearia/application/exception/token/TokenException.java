package com.gasfgrv.barbearia.application.exception.token;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class TokenException extends RuntimeException {

    public TokenException(JWTVerificationException e) {
        super(e.getMessage());
    }

}
