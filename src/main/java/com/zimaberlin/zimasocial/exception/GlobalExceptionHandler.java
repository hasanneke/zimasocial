package com.zimaberlin.zimasocial.exception;

import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.utility.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> resourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode(ex.getCode())
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("bad_credentials")
                .message("Bad Credentials")
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(UnauthorizedException ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("unauthorized")
                .message(ex.getMessage())
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenVerifier.VerificationException.class)
    public ResponseEntity<ResponseError> handleGoogleLoginException(TokenVerifier.VerificationException ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("bad_credentials")
                .message("Google login failed")
                .timeStamp(System.currentTimeMillis())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseError> handleExpiredJWTException(ExpiredJwtException exception){
        return new ResponseEntity<>(ResponseError.builder()
                .timeStamp(System.currentTimeMillis())
                .errorCode("token_expired")
                .message("JWT Token expired")
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("internal_server_error").
                message("Internal Error").build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseError> handleGenericException(ConflictException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("conflict").
                message(ex.getMessage()).build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleGenericException(BadRequestException ex) {
        return new ResponseEntity<>(ResponseError.
                builder().
                timeStamp(System.currentTimeMillis()).
                errorCode("bad_request").
                message(ex.getMessage()).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameterName(), String.format("%s is required", ex.getParameterType()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handlePageNotFound(NoResourceFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        return ResponseEntity.notFound().build();
    }
}
