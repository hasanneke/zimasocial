package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.media.infastructure.TMDBMovieSearcher;
import com.zimaberlin.zimasocial.context.social.media.infastructure.SpotifyMusicSearcher;
import com.zimaberlin.zimasocial.exception.UnauthorizedException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class MediaProxyService {
    private static final List<String> allowedHosts = List.of("api.spotify.com", "api.themoviedb.org", "www.googleapis.com");
    private final RestTemplate restTemplate;
    private final SpotifyMusicSearcher spotifyMusicSearcher;
    private final TMDBMovieSearcher movieTVSearcher;

    @Autowired
    public MediaProxyService(WebClient.Builder builder, SpotifyMusicSearcher spotifyMusicSearcher, TMDBMovieSearcher movieTVSearcher, ServletRequest servletRequest) {
        this.spotifyMusicSearcher = spotifyMusicSearcher;
        this.movieTVSearcher = movieTVSearcher;
        this.restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(@NonNull ClientHttpResponse response) {
                return false;
            }
        });
    }

    public ResponseEntity<byte[]> proxy(HttpMethod method,
                                HttpServletRequest httpServletRequest,
                                @RequestParam(name = "url") String url,
                                MultiValueMap<String, String> params,
                                @RequestBody(required = false) byte[] body) {
        params.remove("url");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParams(params)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        if(!allowedHosts.contains(uri.getHost())){
            throw new UnauthorizedException("URL not supported");
        }
        HttpHeaders headers = new HttpHeaders();
        Collections.list(httpServletRequest.getHeaderNames())
                .stream().filter(e->!e.equals("Authorization") && !e.equals("authorization"))
                .forEach(name -> headers.addAll(name, Collections.list(httpServletRequest.getHeaders(name))));
        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

        if(uri.getHost().equals("api.spotify.com")){
            String accessToken = spotifyMusicSearcher.getAccessToken();
            headers.add("Authorization", "Bearer %s".formatted(accessToken));
        }else if(uri.getHost().equals("api.themoviedb.org")){
            String apiKey = movieTVSearcher.getApiKey();
            headers.add("Authorization", "Bearer " + apiKey);
        }

        ResponseEntity<byte[]> upstream = restTemplate.exchange(uri, method, entity, byte[].class);
        return ResponseEntity.status(upstream.getStatusCode())
                .headers(upstream.getHeaders())
                .body(upstream.getBody());
    }
}
