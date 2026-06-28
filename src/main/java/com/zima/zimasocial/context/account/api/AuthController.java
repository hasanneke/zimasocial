package com.zima.zimasocial.context.account.api;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.context.account.abstracted.AuthService;
import com.zima.zimasocial.context.account.service.AccountService;
import com.zima.zimasocial.context.account.service.LoginCredential;
import com.zima.zimasocial.context.account.service.LoginType;
import com.zima.zimasocial.context.account.value.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping(path = "/api/v1/authentication")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping(path = "/apple-login")
    ResponseEntity<TokenResponse> appleLogin(@RequestParam String token) throws Exception {
        TokenResponse tokenResponse = authService.appleLogin(token);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/v2/google-login")
    ResponseEntity<TokenResponse> googleLoginV2(@RequestParam String token) throws Exception {
        TokenResponse tokenResponse = accountService.login(
                LoginCredential.builder()
                .loginType(LoginType.google)
                .token(token)
                .build());
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping(path = "/refresh-token")
    ResponseEntity<TokenResponse> refreshToken(@RequestParam String refreshToken) throws TokenVerifier.VerificationException, AccountNotFoundException {
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

}
