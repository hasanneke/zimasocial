package com.zimaberlin.zimasocial.utility;


import com.zimaberlin.zimasocial.DTO.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    public String extractId(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwtToken).getPayload();
    }

    public SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isTokenExpired(String jwtToken) {

        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {

        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public TokenResponse generateToken(Long id, String email, String provider) {
        return createToken(id, email, provider);
    }

    public TokenResponse createToken(Long id, String email, String provider) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", id);
        claims.put("email", email);
        claims.put("provider", provider);

        String token = Jwts.builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey())
                .compact();

        TokenResponse refreshToken = TokenResponse.builder().token(Jwts.builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 36))
                .signWith(getSigningKey())
                .compact())
                .expireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 36)).build();

        return TokenResponse.builder()
                .token(token)
                .expireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String jwtToken) {
        return !isTokenExpired(jwtToken);
    }
}
