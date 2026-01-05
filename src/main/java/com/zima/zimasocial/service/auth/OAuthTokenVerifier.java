package com.zima.zimasocial.service.auth;

import com.google.auth.oauth2.TokenVerifier;

public interface OAuthTokenVerifier {
    OAuthTokenResult verify(String token) throws Exception;
}
