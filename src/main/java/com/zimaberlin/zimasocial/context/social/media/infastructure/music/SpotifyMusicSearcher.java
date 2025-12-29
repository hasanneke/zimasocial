package com.zimaberlin.zimasocial.context.social.media.infastructure.music;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import com.zimaberlin.zimasocial.context.social.media.MusicSearcher;
import com.zimaberlin.zimasocial.service.musicService.impl.SpotifyResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SpotifyMusicSearcher implements MusicSearcher {
    private final RestTemplate restTemplate;
    @Getter
    private String accessToken;
    private final ObjectMapper objectMapper;
    String baseUrl;

    @Autowired
    public SpotifyMusicSearcher(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://api.spotify.com/v1/search";
        this.objectMapper = objectMapper;
    }

    @Override
    public MediaItem get(String id) throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/tracks/%s".formatted(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<SpotifyResponse.SpotifyMusic> response = restTemplate.exchange(  // Note: changed to exchange
                url,
                HttpMethod.GET,
                request,
                SpotifyResponse.SpotifyMusic.class
        );
        MediaItem mediaItem = new MediaItem();
        mediaItem.setProvider("spotify");
        mediaItem.setResourceUrl(url);
        mediaItem.setResourceId(id);
        mediaItem.setContent(objectMapper.writeValueAsString(response.getBody()));
        return mediaItem;
    }
    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
