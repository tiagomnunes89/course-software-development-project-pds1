package com.tiagomnunes.aulapds1.resources.exceptions;

import com.tiagomnunes.aulapds1.services.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(
                Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        String error = "Database error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(
                Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation (MethodArgumentNotValidException e, HttpServletRequest request){
        String error = "Validation error";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(
                Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        for(FieldError fieldError : e.getBindingResult().getFieldErrors()){
            err.addError(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(JWTAuthenticationException.class)
    public ResponseEntity<StandardError> jwtAuthentication (JWTAuthenticationException e, HttpServletRequest request) {
        String error = "Authentication error";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError standardError = new StandardError(
                Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(JWTAuthorizationException.class)
    public ResponseEntity<StandardError> jwtAuthorization (JWTAuthorizationException e, HttpServletRequest request) {
        String error = "Authorization error";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(
                Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ParamFormatException.class)
    public ResponseEntity<StandardError> paramFormat (ParamFormatException e, HttpServletRequest request) {
        String error = "Format error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(
                Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
}