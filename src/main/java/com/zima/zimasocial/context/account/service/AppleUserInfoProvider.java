package com.zima.zimasocial.context.account.service;

import com.zima.zimasocial.context.account.abstracted.OAuthTokenVerifier;
import com.zima.zimasocial.context.account.infastructure.AppleTokenVerifier;
import com.zima.zimasocial.context.account.value.OAuthTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleUserInfoProvider implements UserInfoProvider {
    @Override
    public LoginType loginType() {
        return LoginType.apple;
    }
    @Override
    public UserInfo find(LoginCredential loginCredential) throws Exception {
        OAuthTokenVerifier oAuthTokenVerifier = new AppleTokenVerifier();
        OAuthTokenResult oAuthTokenResult = oAuthTokenVerifier.verify(loginCredential.getToken());
        return UserInfo.builder()
                .email(oAuthTokenResult.getEmail())
                .name(oAuthTokenResult.getName())
                .surname(oAuthTokenResult.getSurname())
                .build();
    }
}
