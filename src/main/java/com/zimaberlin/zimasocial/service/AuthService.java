package com.zimaberlin.zimasocial.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.zimaberlin.zimasocial.DTO.TokenResponse;
import com.zimaberlin.zimasocial.entity.Profile;
import com.zimaberlin.zimasocial.repository.ProfileRepository;
import com.zimaberlin.zimasocial.utility.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

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

        Optional<Profile> profile = checkUser(email, "google");

        if(profile.isPresent()){
            return createToken(profile.get());
        }

        Profile profileDto = Profile.builder()
                .email(email)
                .name(name)
                .familyName(familyName)
                .authProvider("google")
                .build();

        Profile createdProfile = saveUser(profileDto);

        return createToken(createdProfile);
    }

    Optional<Profile> checkUser(String email, String provider) {
        return profileRepository.findByEmailAndAuthProvider(email, provider);
    }

    Profile saveUser(Profile profile){
        return profileRepository.save(profile);
    }

    TokenResponse createToken(Profile profile){
        return jwtService.generateToken(profile.getId(), profile.getEmail(), profile.getAuthProvider());
    }
}
