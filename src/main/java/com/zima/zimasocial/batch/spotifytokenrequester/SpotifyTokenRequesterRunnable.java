package com.zima.zimasocial.batch.spotifytokenrequester;

import com.zima.zimasocial.context.social.media.infastructure.SpotifyMusicSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class SpotifyTokenRequesterRunnable implements Runnable {
    private final RestTemplate restTemplate;
    private final SpotifyMusicSearcher spotifyMusicSearcher;
    @Value("${mediaprovider.spotify.clientId}")
    private String clientId;
    @Value("${mediaprovider.spotify.clientSecret}")
    private String clientSecret;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    public SpotifyTokenRequesterRunnable(RestTemplateBuilder restTemplateBuilder, SpotifyMusicSearcher spotifyMusicSearcher) {
        this.restTemplate = restTemplateBuilder.build();
        this.spotifyMusicSearcher = spotifyMusicSearcher;
    }

    @Override
    public void run() {
        logger.debug("Refreshing Spotify token...");
        String url = "https://accounts.spotify.com/api/token";

        // 1️⃣ Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 2️⃣ Prepare body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        // 3️⃣ Create request entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try{
            // 4️⃣ Execute POST
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(url, request, TokenResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                TokenResponse tokenResponse = response.getBody();
                logger.debug("Spotify token refreshed successfully ✅");
                spotifyMusicSearcher.updateAccessToken(tokenResponse.accessToken());
            }
        }catch (Exception e){
            logger.error("Spotify refresh token failed", e);
        }
    }

    record TokenResponse(String accessToken, String tokenType, Integer expiresIn){}
}
