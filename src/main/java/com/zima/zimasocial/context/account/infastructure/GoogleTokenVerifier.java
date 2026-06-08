package com.zima.zimasocial.context.account.infastructure;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.context.account.value.OAuthTokenResult;
import com.zima.zimasocial.context.account.abstracted.OAuthTokenVerifier;

public class GoogleTokenVerifier implements OAuthTokenVerifier {
    @Override
    public OAuthTokenResult verify(String token) throws TokenVerifier.VerificationException {
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();
        JsonWebSignature jsonWebSignature = tokenVerifier.verify(token);
        String email = (String) jsonWebSignature.getPayload().get("email");
        String name = (String) jsonWebSignature.getPayload().get("name");
        String familyName = (String) jsonWebSignature.getPayload().get("family_name");
        return OAuthTokenResult.builder().name(name).surname(familyName).email(email).build();
    }
}
