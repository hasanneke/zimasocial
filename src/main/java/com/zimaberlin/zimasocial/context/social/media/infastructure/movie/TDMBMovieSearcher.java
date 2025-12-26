package com.zimaberlin.zimasocial.context.social.media.infastructure.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import com.zimaberlin.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zimaberlin.zimasocial.context.social.media.MediaCollection;
import com.zimaberlin.zimasocial.context.social.media.MovieSearcher;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.service.movieService.Impl.TDMBMultiMediaTVMovieSearchResponse;
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

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class TDMBMovieSearcher implements MovieSearcher {
    private final RestTemplate restTemplate;
    private final MediaCollection mediaCollection;
    private MediaItemJpaRepository mediaItemJpaRepository;
    private ObjectMapper objectMapper;
    private final String apiKey;
    private final String baseUrl;

    @Autowired
    public TDMBMovieSearcher(MediaItemJpaRepository mediaItemJpaRepository, ObjectMapper objectMapper, RestTemplateBuilder restTemplateBuilder, MediaCollection mediaCollection, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}")String baseUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.mediaCollection = mediaCollection;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.mediaItemJpaRepository = mediaItemJpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public MediaItem getMovie(Integer movieId, MovieMediaType type, String language) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity entity = new HttpEntity<>(headers);
        assert type != null;
        MediaItem mediaItem = new MediaItem();
        mediaItem.setProvider("tdmb");


        if (type.equals(MovieMediaType.movie)) {
            URI uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/movie/%s", baseUrl, movieId))
                    .queryParam("language", "tr-TR")
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUri();
            ResponseEntity<TDMBMultiMediaTVMovieSearchResponse.MovieItem> result = restTemplate.exchange(
                    uriComponentsBuilder,
                    HttpMethod.GET,
                    entity,
                    TDMBMultiMediaTVMovieSearchResponse.MovieItem.class
            );
            mediaItem.setResourceUrl(String.format("%s/movie/%s", baseUrl, movieId));
            mediaItem.setResourceId(movieId.toString());
            mediaItem.setContent(objectMapper.writeValueAsString(result.getBody()));
        }else {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/tv/%s", baseUrl, mediaItem)).queryParam("language", language);
            ResponseEntity<TDMBMultiMediaTVMovieSearchResponse.TvShow> result = restTemplate.exchange(
                    uriComponentsBuilder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    TDMBMultiMediaTVMovieSearchResponse.TvShow.class
            );
            mediaItem.setResourceUrl(String.format("%s/tv/%s", baseUrl, movieId));
            mediaItem.setResourceId(movieId.toString());
            mediaItem.setContent(objectMapper.writeValueAsString(result.getBody()));
        }
        return mediaItem;
    }
}
