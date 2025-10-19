package com.zimaberlin.zimasocial.utility;


import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.context.account.infastructure.repository.RefreshTokenJpaRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final UserJpaRepository userRepository;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public JWTService(RefreshTokenJpaRepository refreshTokenRepository, UserJpaRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

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

    public TokenResponse generateToken(Long id, String email, String provider,  Account account) {
        return createToken(id, email, provider, account);
    }

    public TokenResponse createToken(Long id, String email, String provider, Account account) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", id);
        claims.put("email", email);
        claims.put("provider", provider);
        LocalDateTime tokenExpirationDate = LocalDateTime.now().plusHours(1);
        LocalDateTime refreshTokenExpirationDate = LocalDateTime.now().plusDays(1);
        String token = Jwts.builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();

        String hashedRefreshToken = Jwts.builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 24 * 7))
                .signWith(getSigningKey())
                .compact();

        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setToken(hashedRefreshToken);
        tokenEntity.setExpiresAt(refreshTokenExpirationDate);
        UserEntity user = userRepository.findById(account.getAccountId().getValue()).orElseThrow(UserNotFoundException::new);
        tokenEntity.setUser(user);

        TokenResponse refreshToken = TokenResponse.builder()
                .token(hashedRefreshToken)
                .expireDate(LocalDateTime.now().plusHours(2))
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder()
                .token(token)
                .expireDate(tokenExpirationDate)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String jwtToken) {
        return !isTokenExpired(jwtToken);
    }
}
