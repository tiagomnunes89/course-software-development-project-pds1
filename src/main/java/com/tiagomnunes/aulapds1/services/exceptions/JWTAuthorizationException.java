package com.tiagomnunes.aulapds1.services.exceptions;

public class JWTAuthorizationException extends RuntimeException {

    public JWTAuthorizationException(String message) {
        super(message);
    }
}
