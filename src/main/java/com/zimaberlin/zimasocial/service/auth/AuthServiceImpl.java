package com.zimaberlin.zimasocial.service.auth;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.repository.AccountRepository;
import com.zimaberlin.zimasocial.context.account.service.AccountService;
import com.zimaberlin.zimasocial.context.account.value.CreateAccount;
import com.zimaberlin.zimasocial.entity.RefreshTokenEntity;
import com.zimaberlin.zimasocial.repository.RefreshTokenRepository;
import com.zimaberlin.zimasocial.utility.TokenResponse;
import com.zimaberlin.zimasocial.entity.UserRole;
import com.zimaberlin.zimasocial.exception.DataNotFoundException;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.utility.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final Random random = new Random();
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenResponse googleLogin(String token) throws TokenVerifier.VerificationException {
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();
        JsonWebSignature jsonWebSignature = tokenVerifier.verify(token);
        String email = (String) jsonWebSignature.getPayload().get("email");
        String name = (String) jsonWebSignature.getPayload().get("name");
        String familyName = (String) jsonWebSignature.getPayload().get("family_name");
        Optional<Account> account = accountRepository.findByEmailAndAuthProvider(email, "google");
        if(account.isPresent()){
            if(account.get().getIsDisabled()){
                accountService.activateAccount();
            }
            return createToken(account.get());
        }
        String slug = generateUniqueSlug(name);
        Account newAccount = accountService
                .createAccount(new CreateAccount(email, name, familyName, "google", Set.of(UserRole.regular), slug));
        return createToken(newAccount);
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException {
        boolean expired = jwtService.isTokenExpired(refreshToken);
        if(expired) throw new TokenVerifier.VerificationException("Refresh Token Expired");

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenAndRevoked(refreshToken, false)
                .orElseThrow(()->new DataNotFoundException("Refresh token not found"));
        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        Long userId = Long.parseLong(jwtService.extractId(refreshToken));
        Account account = accountRepository.findByUserId(userId);
        return createToken(account);
    }

    private String generateUniqueSlug(String name) {
        String slug = getTrimmedName(name);

        // Keep trying until we find a unique slug
        int attempt = 0;
        while (userRepository.findBySlugWithDeletedUsers(slug).isPresent()) {
            attempt++;
            slug = slug + random.nextInt(10000);
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
        return jwtService.generateToken(account.getUserId(), account.getEmail(), account.getAuthProvider(), account);
    }
}