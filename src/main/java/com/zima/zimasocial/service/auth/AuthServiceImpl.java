package com.zima.zimasocial.service.auth;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import com.zima.zimasocial.context.account.infastructure.repository.RefreshTokenJpaRepository;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.AccountService;
import com.zima.zimasocial.entity.UserRole;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.service.auth.impl.AppleTokenVerifier;
import com.zima.zimasocial.service.auth.impl.GoogleTokenVerifier;
import com.zima.zimasocial.utility.JWTService;
import com.zima.zimasocial.utility.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final UserJpaRepository userRepository;
    private final JWTService jwtService;
    private final Random random = new Random();
    private final RefreshTokenJpaRepository refreshTokenRepository;

    @Override
    public TokenResponse appleLogin(String token) throws Exception {
        OAuthTokenVerifier oAuthTokenVerifier = new AppleTokenVerifier();
        OAuthTokenResult oAuthTokenResult = oAuthTokenVerifier.verify(token);
        Optional<Account> account = accountRepository.findByEmailAndAuthProvider(oAuthTokenResult.getEmail(), "apple");
        if(account.isPresent()){
            if(account.get().getIsDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(oAuthTokenResult.getName());
        Account newAccount = new Account(new AccountId(accountRepository.nextId()), oAuthTokenResult.getEmail(), oAuthTokenResult.getName(), oAuthTokenResult.getSurname(), "apple", Set.of(UserRole.regular), slug);
        accountService.createAccount(newAccount);
        return createToken(newAccount);
    }

    @Override
    public TokenResponse googleLogin(String token) throws Exception {
        OAuthTokenVerifier oAuthTokenVerifier = new GoogleTokenVerifier();
        OAuthTokenResult oAuthTokenResult = oAuthTokenVerifier.verify(token);
        Optional<Account> account = accountRepository.findByEmailAndAuthProvider(oAuthTokenResult.getEmail(), "google");
        if(account.isPresent()){
            if(account.get().getIsDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(oAuthTokenResult.getName());
        Account newAccount = new Account(new AccountId(accountRepository.nextId()), oAuthTokenResult.getEmail(), oAuthTokenResult.getName(), oAuthTokenResult.getSurname(), "google", Set.of(UserRole.regular), slug);
        accountService.createAccount(newAccount);
        return createToken(newAccount);
    }

    @Override
    public TokenResponse quickLogin() throws TokenVerifier.VerificationException {
        String email = String.format("testmail%s@example.com", random.nextInt());
        String name = "acc%s".formatted(UUID.randomUUID().toString().substring(0, 8));
        String familyName = String.format("accountsur%s", random.nextInt());
        Optional<Account> account = accountRepository.findByEmailAndAuthProvider(email, "google");
        if(account.isPresent()){
            if(account.get().getIsDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(name);
        Account newAccount = new Account(new AccountId(accountRepository.nextId()), email, name, familyName, "google", Set.of(UserRole.regular), slug);
        accountService.createAccount(newAccount);
        return createToken(newAccount);
    }

    @Override
    public TokenResponse quickLoginNext() throws TokenVerifier.VerificationException {
        String email = "hasansabbah0@example.com";
        String name = "Hasan Sabbah";
        String familyName = "Sabbah";
        Optional<Account> account = accountRepository.findByEmailAndAuthProvider(email, "test");
        if(account.isPresent()){
            if(account.get().getIsDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(name);
        Account newAccount = new Account(new AccountId(accountRepository.nextId()), email, name, familyName, "test", Set.of(UserRole.regular), slug);
        accountService.createAccount(newAccount);
        return createToken(newAccount);
    }
    @Override
    public TokenResponse quickLoginPrevious() throws TokenVerifier.VerificationException {
        String email = "zimablue0@example.com";
        String name = "Zima Blue";
        String familyName = "Blue";
        Optional<Account> account = accountRepository.findByEmailAndAuthProvider(email, "test");
        if(account.isPresent()){
            if(account.get().getIsDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(name);
        Account newAccount = new Account(new AccountId(accountRepository.nextId()), email, name, familyName, "test", Set.of(UserRole.regular), slug);
        accountService.createAccount(newAccount);
        return createToken(newAccount);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenAndRevoked(refreshToken, false)
                .orElseThrow(()->new DataNotFoundException("Refresh token not found"));
        boolean expired = refreshTokenEntity.getExpiresAt().isBefore(OffsetDateTime.now());
        if(expired) throw new TokenVerifier.VerificationException("Refresh Token Expired");
        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        Long userId = Long.parseLong(jwtService.extractId(refreshToken));
        Account account = accountRepository.findByUserId(userId);
        return createToken(account);
    }

    private String generateUniqueSlug(String name) {
        String slug = getTrimmedName(name);

        while (userRepository.findBySlugWithDeletedUsers(slug).isPresent()) {
            slug = slug + random.nextInt(10000000);
        }

        return slug;
    }

    private String getTrimmedName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        return name.replaceAll("\\s+", "").toLowerCase();
    }

    private TokenResponse createToken(Account account){
        return jwtService.generateToken(account.getAccountId().getValue(), account.getEmail(), account.getAuthProvider(), account);
    }
}