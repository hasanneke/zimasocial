package com.zima.zimasocial.context.account.abstracted;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.utility.TokenResponse;

import javax.security.auth.login.AccountNotFoundException;

public interface AuthService {
    TokenResponse appleLogin(String token) throws Exception;
    TokenResponse googleLoginV2(String token) throws Exception;
    TokenResponse quickLogin() throws TokenVerifier.VerificationException, AccountNotFoundException;
    TokenResponse quickLoginNext() throws TokenVerifier.VerificationException, AccountNotFoundException;
    TokenResponse quickLoginPrevious() throws TokenVerifier.VerificationException, AccountNotFoundException;
    TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException, AccountNotFoundException;

    TokenResponse slugLogin(String slug) throws AccountNotFoundException;
}
