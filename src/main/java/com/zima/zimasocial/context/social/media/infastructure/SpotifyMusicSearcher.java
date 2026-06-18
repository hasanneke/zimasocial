package com.zima.zimasocial.context.social.media.infastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zima.zimasocial.context.social.media.abstracted.MediaSearcher;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.value.MediaType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service("spotify")
@RequiredArgsConstructor
public class SpotifyMusicSearcher implements MediaSearcher {
    private final RestTemplate restTemplate;
    @Getter
    private String accessToken;

    String baseUrl;

    @Autowired
    public SpotifyMusicSearcher(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://api.spotify.com/v1/search";
    }

    @Override
    public Media get(String id) throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/tracks/%s".formatted(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        headers.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(  // Note: changed to exchange
                url,
                HttpMethod.GET,
                request,
                String.class
        );
        Media media = new Media();
        media.setProvider("spotify");
        media.setType(MediaType.music);
        media.setResourceUrl(url);
        media.setResourceId(id);
        media.setContent(response.getBody());
        return media;
    }
    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
