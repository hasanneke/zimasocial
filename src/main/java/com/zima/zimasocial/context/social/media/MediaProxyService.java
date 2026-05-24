package com.zima.zimasocial.context.social.media;

import com.zima.zimasocial.context.social.media.infastructure.SpotifyMusicSearcher;
import com.zima.zimasocial.context.social.media.infastructure.TMDBMovieSearcher;
import com.zima.zimasocial.exception.UnauthorizedException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MediaProxyService {
    private static final List<String> allowedHosts = List.of("api.spotify.com", "api.themoviedb.org", "www.googleapis.com");
    private final RestTemplate restTemplate;
    private final SpotifyMusicSearcher spotifyMusicSearcher;
    private final TMDBMovieSearcher movieTVSearcher;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private static final List<String> BLOCKED_RESPONSE_HEADERS = List.of(
            "transfer-encoding",
            "connection",
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "upgrade",
            "content-length"
    );
    @Autowired
    public MediaProxyService(SpotifyMusicSearcher spotifyMusicSearcher, TMDBMovieSearcher movieTVSearcher, ServletRequest servletRequest) {
        this.spotifyMusicSearcher = spotifyMusicSearcher;
        this.movieTVSearcher = movieTVSearcher;
        this.restTemplate = new RestTemplate();
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
        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

        if(uri.getHost().equals("api.spotify.com")){
            String accessToken = spotifyMusicSearcher.getAccessToken();
            headers.add("Authorization", "Bearer %s".formatted(accessToken));
        }else if(uri.getHost().equals("api.themoviedb.org")){
            String apiKey = movieTVSearcher.getApiKey();
            headers.add("Authorization", "Bearer " + apiKey);
        }
       try{
           ResponseEntity<byte[]> upstream = restTemplate.exchange(uri, method, entity, byte[].class);
           headers.setContentType(MediaType.APPLICATION_JSON);
           headers.setContentLength(upstream.getBody() != null ? upstream.getBody().length : 0);
           return ResponseEntity.status(upstream.getStatusCode())
                   .headers(filterResponseHeaders(upstream.getHeaders()))
                   .body(upstream.getBody());
       }catch (HttpClientErrorException httpClientErrorException){
           logger.error("Proxy search failed", httpClientErrorException);
           return ResponseEntity.status(httpClientErrorException.getStatusCode())
                   .headers(httpClientErrorException.getResponseHeaders())
                   .body(httpClientErrorException.getResponseBodyAs(byte[].class));
       }catch (RestClientException restClientException){
           logger.error("Proxy search failed", restClientException);
           return ResponseEntity.internalServerError().build();
       }catch (Exception exception){
           logger.error("Proxy search failed", exception);
           return ResponseEntity.internalServerError().build();
       }
    }
    private HttpHeaders filterResponseHeaders(HttpHeaders upstreamHeaders) {
        HttpHeaders filtered = new HttpHeaders();

        if (upstreamHeaders == null) {
            filtered.setContentType(MediaType.APPLICATION_JSON);
            return filtered;
        }

        upstreamHeaders.forEach((name, values) -> {
            if (!BLOCKED_RESPONSE_HEADERS.contains(name.toLowerCase())) {
                filtered.put(name, values);
            }
        });

        return filtered;
    }

}

