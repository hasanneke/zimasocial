package com.zima.zimasocial.context.account.abstracted;

import com.zima.zimasocial.context.account.value.OAuthTokenResult;

public interface OAuthTokenVerifier {
    OAuthTokenResult verify(String token) throws Exception;
}
