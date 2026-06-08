package com.zima.zimasocial.controller;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.context.account.abstracted.AuthService;
import com.zima.zimasocial.utility.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

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

    @GetMapping(path = "/v2/google-login")
    ResponseEntity<TokenResponse> googleLoginV2(@RequestParam String token) throws Exception {
        TokenResponse tokenResponse = authService.googleLoginV2(token);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/refresh-token")
    ResponseEntity<TokenResponse> refreshToken(@RequestParam String refreshToken) throws TokenVerifier.VerificationException, AccountNotFoundException {
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

}
