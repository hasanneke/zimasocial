package com.zimaberlin.zimasocial.service.Auth;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.domain.TokenResponse;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.entity.UserRole;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.Auth.AuthService;
import com.zimaberlin.zimasocial.utility.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private JWTService jwtService;
    private final Random random = new Random();

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse googleLogin(String token) throws TokenVerifier.VerificationException {
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();
        JsonWebSignature jsonWebSignature = tokenVerifier.verify(token);
        String email = (String) jsonWebSignature.getPayload().get("email");
        String name = (String) jsonWebSignature.getPayload().get("name");
        String familyName = (String) jsonWebSignature.getPayload().get("family_name");

        Optional<UserEntity> profile = checkUser(email, "google");

        if(profile.isPresent()){
            return createToken(profile.get());
        }

        String slug = generateUniqueSlug(email);

        UserEntity profileDto = UserEntity.builder()
                .email(email)
                .name(name)
                .familyName(familyName)
                .authProvider("google")
                .roles(Set.of(UserRole.regular))
                .slug(slug)
                .build();

        UserEntity createdProfile = saveUser(profileDto);

        return createToken(createdProfile);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) throws TokenVerifier.VerificationException {
        boolean expired = jwtService.isTokenExpired(refreshToken);
        if(expired) throw new TokenVerifier.VerificationException("Refresh Token Expired");
        Long userId = Long.parseLong(jwtService.extractId(refreshToken));
        UserEntity user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return createToken(user);
    }

    private String generateUniqueSlug(String email) {
        String baseSlug = getTrimmedName(email);
        String slug = baseSlug;

        // Keep trying until we find a unique slug
        int attempt = 0;
        while (userRepository.findBySlug(slug).isPresent()) {
            attempt++;
            slug = baseSlug + random.nextInt(1000);  // Add random number between 0-999
        }

        return slug;
    }

    private String getTrimmedName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        return name.trim();
    }

    private Optional<UserEntity> checkUser(String email, String provider) {
        return userRepository.findByEmailAndAuthProvider(email, provider);
    }

    private UserEntity saveUser(UserEntity profile){
        return userRepository.save(profile);
    }

    private TokenResponse createToken(UserEntity profile){
        return jwtService.generateToken(profile.getId(), profile.getEmail(), profile.getAuthProvider());
    }
}