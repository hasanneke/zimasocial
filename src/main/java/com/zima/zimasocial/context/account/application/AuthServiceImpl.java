package com.zima.zimasocial.context.account.application;

import com.google.auth.oauth2.TokenVerifier;
import com.zima.zimasocial.context.account.abstracted.AuthService;
import com.zima.zimasocial.context.account.abstracted.OAuthTokenVerifier;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.event.AccountCreatedEvent;
import com.zima.zimasocial.context.account.exception.AccountBannedException;
import com.zima.zimasocial.context.account.factory.AccountFactory;
import com.zima.zimasocial.context.account.infastructure.AppleTokenVerifier;
import com.zima.zimasocial.context.account.infastructure.GoogleTokenVerifier;
import com.zima.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import com.zima.zimasocial.context.account.infastructure.repository.RefreshTokenJpaRepository;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.AccountService;
import com.zima.zimasocial.context.account.service.LoginType;
import com.zima.zimasocial.context.account.value.AccountIdentity;
import com.zima.zimasocial.context.account.value.OAuthTokenResult;
import com.zima.zimasocial.context.account.value.PersonalInfo;
import com.zima.zimasocial.context.account.value.UserRole;
import com.zima.zimasocial.shared.exception.DataNotFoundException;
import com.zima.zimasocial.shared.exception.UnauthorizedException;
import com.zima.zimasocial.context.account.service.JWTService;
import com.zima.zimasocial.context.account.value.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
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
    private final AccountFactory accountFactory;
    private final JWTService jwtService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Random random = new Random();
    private final RefreshTokenJpaRepository refreshTokenRepository;

    @Override
    @Transactional
    public TokenResponse appleLogin(String token) throws Exception {
        OAuthTokenVerifier oAuthTokenVerifier = new AppleTokenVerifier();
        OAuthTokenResult oAuthTokenResult = oAuthTokenVerifier.verify(token);
        Optional<Account> account = accountRepository.findByEmailAndLoginType(oAuthTokenResult.getEmail(), LoginType.apple);
        if(account.isPresent()){
            if(account.get().isBanned()){
                throw new AccountBannedException();
            }
            if(account.get().isDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        Account newAccount = accountFactory.createOAuth2Account(oAuthTokenResult, LoginType.apple);
        accountRepository.save(newAccount);
        TokenResponse response = createToken(newAccount);
        applicationEventPublisher.publishEvent(new AccountCreatedEvent(newAccount.getAccountId().getValue()));
        return response;
    }

    @Override
    @Transactional
    public TokenResponse googleLoginV2(String token) throws Exception {
        OAuthTokenVerifier oAuthTokenVerifier = new GoogleTokenVerifier();
        OAuthTokenResult oAuthTokenResult = oAuthTokenVerifier.verify(token);
        Optional<Account> account = accountRepository.findByEmailAndLoginType(oAuthTokenResult.getEmail(), LoginType.google);
        if(account.isPresent()){
            if(account.get().isBanned()){
                throw new AccountBannedException();
            }
            if(account.get().isDisabled()){
                accountService.activateAccount(account.get());
            }
            return createRefreshToken(account.get());
        }
        Account newAccount = accountFactory.createOAuth2Account(oAuthTokenResult, LoginType.google);
        accountRepository.save(newAccount);
        TokenResponse tokenResponse = createRefreshToken(newAccount);
        applicationEventPublisher.publishEvent(new AccountCreatedEvent(newAccount.getAccountId().getValue()));
        return tokenResponse;
    }

    @Override
    public TokenResponse quickLogin() throws AccountNotFoundException {
        String email = String.format("testmail%s@example.com", random.nextInt());
        String name = "acc%s".formatted(UUID.randomUUID().toString().substring(0, 8));
        String familyName = String.format("accountsur%s", random.nextInt());
        Optional<Account> account = accountRepository.findByEmailAndLoginType(email, LoginType.google);
        if(account.isPresent()){
            if(account.get().isDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(name);
        AccountIdentity accountIdentity = AccountIdentity
                .builder()
                .accountId(new AccountId(accountRepository.nextId()))
                .email(email)
                .loginType(LoginType.google)
                .slug(slug)
                .roles(Set.of(UserRole.regular))
                .build();
        PersonalInfo personalInfo = PersonalInfo
                .builder()
                .name(name)
                .surname(familyName)
                .build();

        Account newAccount = Account.newAccount(accountIdentity, personalInfo);
        accountRepository.save(newAccount);
        applicationEventPublisher.publishEvent(new AccountCreatedEvent(newAccount.getAccountId().getValue()));
        return createToken(newAccount);
    }

    @Override
    public TokenResponse quickLoginNext() throws AccountNotFoundException {
        String email = "hasansabbah0@example.com";
        String name = "Hasan Sabbah";
        String familyName = "Sabbah";
        Optional<Account> account = accountRepository.findByEmailAndLoginType(email, LoginType.google);
        if(account.isPresent()){
            if(account.get().isDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(name);
        AccountIdentity accountIdentity = AccountIdentity
                .builder()
                .accountId(new AccountId(accountRepository.nextId()))
                .email(email)
                .loginType(LoginType.google)
                .slug(slug)
                .roles(Set.of(UserRole.regular))
                .build();
        PersonalInfo personalInfo = PersonalInfo
                .builder()
                .name(name)
                .surname(familyName)
                .build();

        Account newAccount = Account.newAccount(accountIdentity, personalInfo);
        accountRepository.save(newAccount);
        return createToken(newAccount);
    }
    @Override
    public TokenResponse quickLoginPrevious() throws AccountNotFoundException {
        String email = "zimablue0@example.com";
        Optional<Account> account = accountRepository.findByEmailAndLoginType(email, LoginType.google);
        if(account.isPresent()){
            if(account.get().isDisabled()){
                accountService.activateAccount(account.get());
            }
            return createToken(account.get());
        }
        Account newAccount = accountFactory.createZimaAccount();
        accountRepository.save(newAccount);
        return createToken(newAccount);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException, AccountNotFoundException {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenAndRevoked(refreshToken, false)
                .orElseThrow(() -> new DataNotFoundException("Refresh token not found"));
        boolean expired = refreshTokenEntity.getExpiresAt().isBefore(OffsetDateTime.now());
        if(expired) throw new TokenVerifier.VerificationException("Refresh Token Expired");
        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);
        Long userId = Long.parseLong(jwtService.extractId(refreshToken));
        Account account = accountRepository.findByAccountId(new AccountId(userId)).orElseThrow(AccountNotFoundException::new);
        if(account.isBanned()){
            throw new UnauthorizedException();
        }
        return createRefreshToken(account);
    }

    @Override
    public TokenResponse slugLogin(String slug) throws AccountNotFoundException {
        return createRefreshToken(accountRepository.findBySlug(slug).orElseThrow(AccountNotFoundException::new));
    }

    private String generateUniqueSlug(String name) {
        String slug = getTrimmedName(name);

        while (accountRepository.findBySlugWithDeletedUsers(slug).isPresent()) {
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

    private TokenResponse createToken(Account account) throws AccountNotFoundException {
        return jwtService.generateToken(
                account.getAccountId().getValue(),
                account.getEmail(),
                account.getLoginType().name(),
                account);
    }
    private TokenResponse createRefreshToken(Account account) {
        TokenResponse tokenResponse = jwtService.createShortLivedToken(account);
        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setToken(tokenResponse.getRefreshToken().getToken());
        tokenEntity.setExpiresAt(tokenResponse.getRefreshToken().getExpireDate());
        tokenEntity.setUserId(account.getAccountId().getValue());
        refreshTokenRepository.save(tokenEntity);
        return tokenResponse;
    }
}