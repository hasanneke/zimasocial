package com.zima.zimasocial.context.social.media.infastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zima.zimasocial.context.social.media.entity.MediaItem;
import com.zima.zimasocial.context.social.media.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.abstracted.MediaSearcher;
import com.zima.zimasocial.entity.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service("google_books")
public class GoogleBookSearcher implements MediaSearcher {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public GoogleBookSearcher(RestTemplateBuilder restTemplateBuilder, MediaItemJpaRepository mediaItemJpaRepository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://www.googleapis.com";
    }

    @Override
    @Transactional
    public MediaItem get(String id) throws JsonProcessingException {
        String url = String.format("%s/books/v1/volumes/%s", baseUrl, id);
        URI uri = UriComponentsBuilder.fromHttpUrl(String.format("%s/books/v1/volumes/%s", baseUrl, id))
                .queryParam("language", "tr-TR")
                .queryParam("key", "AIzaSyAZqdd_sxpQ05uqClGFl4AqeELF1XY4BEE")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        ResponseEntity<String> response = restTemplate
                .exchange(uri,
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        String.class);
        MediaItem mediaItem = new MediaItem();
        mediaItem.setProvider("googleBooks");
        mediaItem.setType(MediaType.book);
        mediaItem.setResourceUrl(url);
        mediaItem.setResourceId(id);
        mediaItem.setContent(response.getBody());
        return mediaItem;
    }
}
