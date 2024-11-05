package com.zimaberlin.zimasocial.service.auth;

import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.utility.TokenResponse;

public interface AuthService {
    TokenResponse googleLogin(String token) throws TokenVerifier.VerificationException;
    TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException;
}
