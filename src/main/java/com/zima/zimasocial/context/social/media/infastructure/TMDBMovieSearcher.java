package com.zima.zimasocial.context.social.media.infastructure;

import com.zima.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.MediaSearcher;
import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service("tmdb_movie")
public class TMDBMovieSearcher implements MediaSearcher {
    private final RestTemplate restTemplate;
    @Getter
    private final String apiKey;
    private final String baseUrl;

    @Autowired
    public TMDBMovieSearcher(MediaItemJpaRepository mediaItemJpaRepository, RestTemplateBuilder restTemplateBuilder, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}") String baseUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public MediaItem get(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity entity = new HttpEntity<>(headers);
        MediaItem mediaItem = new MediaItem();
        mediaItem.setProvider("tdmb");

        URI uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/movie/%s", baseUrl, Integer.parseInt(id)))
                .queryParam("language", "tr-TR")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        ResponseEntity<String> result = restTemplate.exchange(
                uriComponentsBuilder,
                HttpMethod.GET,
                entity,
                String.class
        );
        mediaItem.setResourceUrl(String.format("%s/movie/%s", baseUrl, id));
        mediaItem.setResourceId(id);
        mediaItem.setType(MediaType.movie);
        mediaItem.setContent(result.getBody());
        return mediaItem;
    }
}
