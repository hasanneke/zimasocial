package com.zima.zimasocial.controller;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.context.account.abstracted.AuthService;
import com.zima.zimasocial.utility.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/authentication")
@Profile({"dev"})
public class AuthControllerDev {
    private final AuthService authService;

    @GetMapping(path = "/quick-login")
    ResponseEntity<TokenResponse> quickLogin() throws TokenVerifier.VerificationException, AccountNotFoundException {
        TokenResponse tokenResponse = authService.quickLogin();
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/quick-login-test")
    ResponseEntity<TokenResponse> quickLoginTest() throws TokenVerifier.VerificationException, AccountNotFoundException {
        TokenResponse tokenResponse = authService.quickLoginNext();
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/quick-login-test2")
    ResponseEntity<TokenResponse> quickLoginTest2() throws TokenVerifier.VerificationException, AccountNotFoundException {
        TokenResponse tokenResponse = authService.quickLoginPrevious();
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/slug-login/{slug}")
    ResponseEntity<TokenResponse> slugLogin(@PathVariable String slug) throws AccountNotFoundException {
        return ResponseEntity.ok(authService.slugLogin(slug));
    }
}
