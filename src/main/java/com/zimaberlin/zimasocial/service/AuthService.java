package com.zimaberlin.zimasocial.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.domain.TokenResponse;
import com.zimaberlin.zimasocial.entity.ProfileEntity;
import com.zimaberlin.zimasocial.entity.UserRole;
import com.zimaberlin.zimasocial.repository.ProfileRepository;
import com.zimaberlin.zimasocial.utility.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private ProfileRepository profileRepository;
    private JWTService jwtService;

    @Autowired
    public AuthService(ProfileRepository profileRepository, JWTService jwtService) {
        this.profileRepository = profileRepository;
        this.jwtService = jwtService;
    }

    public TokenResponse googleLogin(String token) throws TokenVerifier.VerificationException {
        TokenVerifier tokenVerifier = TokenVerifier.newBuilder().build();
        JsonWebSignature jsonWebSignature = tokenVerifier.verify(token);
        String email = (String) jsonWebSignature.getPayload().get("email");
        String name = (String) jsonWebSignature.getPayload().get("name");
        String familyName = (String) jsonWebSignature.getPayload().get("family_name");

        Optional<ProfileEntity> profile = checkUser(email, "google");

        if(profile.isPresent()){
            return createToken(profile.get());
        }

        ProfileEntity profileDto = ProfileEntity.builder()
                .email(email)
                .name(name)
                .familyName(familyName)
                .authProvider("google")
                .roles(Set.of(UserRole.regular))
                .build();

        ProfileEntity createdProfile = saveUser(profileDto);

        return createToken(createdProfile);
    }

    Optional<ProfileEntity> checkUser(String email, String provider) {
        return profileRepository.findByEmailAndAuthProvider(email, provider);
    }

    ProfileEntity saveUser(ProfileEntity profile){
        return profileRepository.save(profile);
    }

    TokenResponse createToken(ProfileEntity profile){
        return jwtService.generateToken(profile.getId(), profile.getEmail(), profile.getAuthProvider());
    }
}
