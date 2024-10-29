package com.zimaberlin.zimasocial.service.Auth;

import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.domain.TokenResponse;

public interface AuthService {
    TokenResponse googleLogin(String token) throws TokenVerifier.VerificationException;
    TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException;
}
