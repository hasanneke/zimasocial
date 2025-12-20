package com.zimaberlin.zimasocial.batch.spotifytokenrequester;

import com.zimaberlin.zimasocial.context.social.media.infastructure.music.SpotifyMusicSearcher;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicClient;
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

    @Autowired
    public SpotifyTokenRequesterRunnable(RestTemplateBuilder restTemplateBuilder, SpotifyMusicSearcher spotifyMusicSearcher) {
        this.restTemplate = restTemplateBuilder.build();
        this.spotifyMusicSearcher = spotifyMusicSearcher;
    }

    @Override
    public void run() {
        System.out.println("Refreshing Spotify token...");

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

        // 4️⃣ Execute POST
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(url, request, TokenResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            TokenResponse tokenResponse = response.getBody();
            System.out.println("Spotify token refreshed successfully ✅");
            spotifyMusicSearcher.updateAccessToken(tokenResponse.accessToken());
        } else {
            System.err.println("❌ Failed to refresh Spotify token: " + response.getStatusCode());
        }
    }

    record TokenResponse(String accessToken, String tokenType, Integer expiresIn){}
}
