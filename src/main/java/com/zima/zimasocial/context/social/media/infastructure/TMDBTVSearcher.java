package com.zima.zimasocial.context.social.media.infastructure;

import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.abstracted.MediaSearcher;
import com.zima.zimasocial.context.social.media.value.MediaType;
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

    public Media get(String tvId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity entity = new HttpEntity<>(headers);
        Media media = new Media();
        media.setProvider("tdmb");

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/tv/%s", baseUrl, tvId)).queryParam("language", "tr");
        ResponseEntity<String> result = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );
        media.setResourceUrl(String.format("%s/tv/%s", baseUrl, tvId));
        media.setResourceId(tvId);
        media.setType(MediaType.tv);
        media.setContent(result.getBody());
        return media;
    }
}
