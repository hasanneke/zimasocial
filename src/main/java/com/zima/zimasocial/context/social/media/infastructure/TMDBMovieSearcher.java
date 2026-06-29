package com.zima.zimasocial.context.social.media.infastructure;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.abstracted.MediaSearcher;
import com.zima.zimasocial.context.social.media.value.MediaId;
import com.zima.zimasocial.context.social.media.value.MediaType;
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

    public Media get(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity entity = new HttpEntity<>(headers);
        Media media = new Media();
        media.setProvider("tdmb");

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
        media.setId(new MediaId(UuidCreator.getTimeOrdered()));
        media.setResourceUrl(String.format("%s/movie/%s", baseUrl, id));
        media.setResourceId(id);
        media.setType(MediaType.movie);
        media.setContent(result.getBody());
        return media;
    }
}
