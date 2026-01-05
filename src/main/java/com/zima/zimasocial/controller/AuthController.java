package com.zima.zimasocial.controller;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.utility.TokenResponse;
import com.zima.zimasocial.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/authentication")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping(path = "/apple-login")
    ResponseEntity<TokenResponse> appleLogin(@RequestParam String token) throws Exception {
        TokenResponse tokenResponse = authService.appleLogin(token);
        return ResponseEntity.ok(tokenResponse);
    }
    @GetMapping(path = "/google-login")
    ResponseEntity<TokenResponse> googleLogin(@RequestParam String token) throws Exception {
        TokenResponse tokenResponse = authService.googleLogin(token);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/refresh-token")
    ResponseEntity<TokenResponse> refreshToken(@RequestParam String refreshToken) throws TokenVerifier.VerificationException {
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
    @Profile({"dev"})
    @GetMapping(path = "/quick-login")
    ResponseEntity<TokenResponse> quickLogin() throws TokenVerifier.VerificationException {
        TokenResponse tokenResponse = authService.quickLogin();
        return ResponseEntity.ok(tokenResponse);
    }
    @Profile({"dev"})
    @GetMapping(path = "/quick-login-test")
    ResponseEntity<TokenResponse> quickLoginTest() throws TokenVerifier.VerificationException {
        TokenResponse tokenResponse = authService.quickLoginNext();
        return ResponseEntity.ok(tokenResponse);
    }
    @Profile({"dev"})
    @GetMapping(path = "/quick-login-test2")
    ResponseEntity<TokenResponse> quickLoginTest2() throws TokenVerifier.VerificationException {
        TokenResponse tokenResponse = authService.quickLoginPrevious();
        return ResponseEntity.ok(tokenResponse);
    }
}
