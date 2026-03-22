package com.zima.zimasocial.service.auth;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.utility.TokenResponse;

public interface AuthService {
    TokenResponse appleLogin(String token) throws Exception;
    TokenResponse googleLogin(String token) throws Exception;
    TokenResponse googleLoginV2(String token) throws Exception;
    TokenResponse quickLogin() throws TokenVerifier.VerificationException;
    TokenResponse quickLoginNext() throws TokenVerifier.VerificationException;
    TokenResponse quickLoginPrevious() throws TokenVerifier.VerificationException;
    TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException;

    TokenResponse slugLogin(String slug);
}
