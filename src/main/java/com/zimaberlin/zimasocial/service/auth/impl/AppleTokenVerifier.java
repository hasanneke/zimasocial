package com.zimaberlin.zimasocial.service.auth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.zimaberlin.zimasocial.service.auth.OAuthTokenResult;
import com.zimaberlin.zimasocial.service.auth.OAuthTokenVerifier;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;

public class AppleTokenVerifier implements OAuthTokenVerifier {
    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";
    private static final String APPLE_ISSUER = "https://appleid.apple.com";
    private static final String CLIENT_ID = "com.zimasocial.zimasocial"; // Your Service ID / Bundle ID
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public OAuthTokenResult verify(String token) throws Exception {
        // Parse the JWT without verifying yet
        SignedJWT signedJWT = SignedJWT.parse(token);
        String kid = signedJWT.getHeader().getKeyID();

        // Fetch Apple's public keys
        JWKSet jwkSet = JWKSet.load(new URL(APPLE_KEYS_URL));
        JWK jwk = jwkSet.getKeyByKeyId(kid);

        if (jwk == null || !(jwk instanceof RSAKey)) {
            throw new Exception("Apple public key not found or invalid");
        }

        RSAKey rsaKey = (RSAKey) jwk;

        // Verify the signature
        boolean valid = signedJWT.verify(new com.nimbusds.jose.crypto.RSASSAVerifier(rsaKey.toRSAPublicKey()));
        if (!valid) throw new Exception("Invalid Apple ID token signature");

        // Verify claims
        var claims = signedJWT.getJWTClaimsSet();

        if (!APPLE_ISSUER.equals(claims.getIssuer())) throw new Exception("Invalid issuer");
        if (!CLIENT_ID.equals(claims.getAudience().get(0))) throw new Exception("Invalid audience");
        if (new Date().after(claims.getExpirationTime())) throw new Exception("Token expired");

        return OAuthTokenResult.builder().name(Arrays.stream(claims.getStringClaim("email").split("@")).findFirst().get()).email(claims.getStringClaim("email")).build();
    }
}
