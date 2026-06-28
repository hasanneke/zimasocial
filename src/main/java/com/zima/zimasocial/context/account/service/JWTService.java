package com.zima.zimasocial.context.account.service;


import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import com.zima.zimasocial.context.account.infastructure.repository.RefreshTokenJpaRepository;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.value.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.security.auth.login.AccountNotFoundException;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final AccountRepository accountRepository;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public JWTService(RefreshTokenJpaRepository refreshTokenRepository, AccountRepository accountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountRepository = accountRepository;
    }

    public String extractId(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSigningKey()).clockSkewSeconds(30).build().parseSignedClaims(jwtToken).getPayload();
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

    public TokenResponse generateToken(Long id, String email, String provider, Account account) throws AccountNotFoundException {
        return createToken(id, email, provider, account);
    }
    public TokenResponse generateToken(Account account) throws AccountNotFoundException {
        return createToken(account.getAccountId().getValue(), account.getEmail(), account.getLoginType().name(), account);
    }
    public TokenResponse createRefreshToken(Account account) {
        TokenResponse tokenResponse = createShortLivedToken(account);
        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setUser(account);
        tokenEntity.setToken(tokenResponse.getRefreshToken().getToken());
        tokenEntity.setExpiresAt(tokenResponse.getRefreshToken().getExpireDate());
        tokenEntity.setUserId(account.getAccountId().getValue());
        refreshTokenRepository.save(tokenEntity);
        return tokenResponse;
    }

    public TokenResponse createShortLivedToken(Account account) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", account.getAccountId().getValue());
        claims.put("email", account.getEmail());
        claims.put("provider", account.getLoginType());
        OffsetDateTime tokenExpirationDate = OffsetDateTime.now().plusMinutes(5);
        OffsetDateTime refreshTokenExpirationDate = OffsetDateTime.now().plusMonths(2);

        String token = Jwts.builder()
                .subject(String.valueOf(account.getAccountId().getValue()))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 5))
                .signWith(getSigningKey())
                .compact();

        String hashedRefreshToken = Jwts.builder()
                .subject(String.valueOf(account.getAccountId().getValue()))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60))
                .signWith(getSigningKey())
                .compact();
        TokenResponse refreshToken = TokenResponse.builder()
                .token(hashedRefreshToken)
                .expireDate(refreshTokenExpirationDate)
                .build();

        return TokenResponse.builder()
                .token(token)
                .expireDate(tokenExpirationDate)
                .refreshToken(refreshToken)
                .build();
    }

    private TokenResponse createToken(Long id, String email, String provider, Account account) throws AccountNotFoundException {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", id);
        claims.put("email", email);
        claims.put("provider", provider);
        OffsetDateTime tokenExpirationDate = OffsetDateTime.now().plusMonths(2);
        OffsetDateTime refreshTokenExpirationDate = OffsetDateTime.now().plusMonths(2);

        String token = Jwts.builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60))
                .signWith(getSigningKey())
                .compact();

        String hashedRefreshToken = Jwts.builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60))
                .signWith(getSigningKey())
                .compact();

        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setToken(hashedRefreshToken);
        tokenEntity.setExpiresAt(refreshTokenExpirationDate);

        Account user = accountRepository.findById(account.getAccountId()).orElseThrow(AccountNotFoundException::new);
        tokenEntity.setUser(user);
        tokenEntity.setUserId(user.getAccountId().getValue());

        TokenResponse refreshToken = TokenResponse.builder()
                .token(hashedRefreshToken)
                .expireDate(refreshTokenExpirationDate)
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
