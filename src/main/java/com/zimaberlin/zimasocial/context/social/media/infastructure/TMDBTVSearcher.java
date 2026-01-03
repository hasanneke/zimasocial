package com.zimaberlin.zimasocial.context.social.media.infastructure;

import com.zimaberlin.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zimaberlin.zimasocial.context.social.media.MediaSearcher;
import com.zimaberlin.zimasocial.entity.MediaType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service("tmdb_tv")
public class TMDBTVSearcher implements MediaSearcher {
    private final RestTemplate restTemplate;
    @Getter
    private final String apiKey;
    private final String baseUrl;

    @Autowired
    public TMDBTVSearcher(MediaItemJpaRepository mediaItemJpaRepository, RestTemplateBuilder restTemplateBuilder, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}") String baseUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public MediaItem get(String tvId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity entity = new HttpEntity<>(headers);
        MediaItem mediaItem = new MediaItem();
        mediaItem.setProvider("tdmb");

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/tv/%s", baseUrl, tvId)).queryParam("language", "tr");
        ResponseEntity<String> result = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );
        mediaItem.setResourceUrl(String.format("%s/tv/%s", baseUrl, tvId));
        mediaItem.setResourceId(tvId);
        mediaItem.setType(MediaType.tv);
        mediaItem.setContent(result.getBody());
        return mediaItem;
    }
}
