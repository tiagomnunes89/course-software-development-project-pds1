package com.tiagomnunes.aulapds1.services.exceptions;

public class JWTAuthenticationException extends RuntimeException {

    public JWTAuthenticationException(String message) {
        super(message);
    }
}
