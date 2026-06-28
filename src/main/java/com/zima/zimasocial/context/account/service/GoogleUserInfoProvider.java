package com.zima.zimasocial.context.account.service;

import com.zima.zimasocial.context.account.abstracted.OAuthTokenVerifier;
import com.zima.zimasocial.context.account.infastructure.GoogleTokenVerifier;
import com.zima.zimasocial.context.account.value.OAuthTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleUserInfoProvider implements UserInfoProvider {
    @Override
    public LoginType loginType() {
        return LoginType.google;
    }

    @Override
    public UserInfo find(LoginCredential loginCredential) throws Exception {
        OAuthTokenVerifier oAuthTokenVerifier = new GoogleTokenVerifier();
        OAuthTokenResult oAuthTokenResult = oAuthTokenVerifier.verify(loginCredential.getToken());
        return UserInfo.builder()
                .name(oAuthTokenResult.getName())
                .surname(oAuthTokenResult.getSurname())
                .email(oAuthTokenResult.getEmail())
                .build();
    }
}
