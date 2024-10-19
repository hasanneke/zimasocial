package com.zimaberlin.zimasocial.exception;

import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.utility.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
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
    @ExceptionHandler(TokenVerifier.VerificationException.class)
    public ResponseEntity<ResponseError> handleGoogleLoginException(TokenVerifier.VerificationException ex) {
        return new ResponseEntity<>(ResponseError.builder()
                .errorCode("bad_credentials")
                .message("Google login failed")
                .timeStamp(System.currentTimeMillis())
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
}
