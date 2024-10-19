package com.zimaberlin.zimasocial.controller;

import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.DTO.TokenResponse;
import com.zimaberlin.zimasocial.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(path = "/google-login")
    ResponseEntity<TokenResponse> googleLogin(@RequestParam String token) throws TokenVerifier.VerificationException {
        TokenResponse tokenResponse = authService.googleLogin(token);
        return ResponseEntity.ok(tokenResponse);
    }
}
