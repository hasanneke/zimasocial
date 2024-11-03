package com.zimaberlin.zimasocial.service.MusicService;

import com.zimaberlin.zimasocial.service.MusicService.DTOs.SpotifyResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class SpotifyMusicService {
    private final RestTemplate restTemplate;
    String baseUrl;

    public SpotifyMusicService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://api.spotify.com/v1/search";
    }

    public SpotifyResponse.SpotifyTracks searchTracks(String query, int offset, int limit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("type", "track")
                .queryParam("q", query)
                .queryParam("offset", offset)
                .queryParam("limit", limit);

        headers.set("Authorization", "Bearer BQDGT6qJxGZEac8c8HG-nJY0iKuqKtlhQ-wRfpslmVQH0ji2F4aLkOYVrvvSNhEhO6qiar9vhS42dxQ9HM6g2YlhbCriaPnI4tPCMXChUpFfBAM_-W4");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Create request entity
        HttpEntity<SpotifyResponse> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,request, SpotifyResponse.class);
//        System.out.println(response);
       return response.getBody().getTracks();

    }
}
