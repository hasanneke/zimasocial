package com.zimaberlin.zimasocial.service.movieService.Impl;

import com.zimaberlin.zimasocial.controller.SearchMultimediaController;
import com.zimaberlin.zimasocial.service.movieService.domain.MovieResponseView;
import com.zimaberlin.zimasocial.service.movieService.domain.SearchMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class TDMBMovieService implements SearchMovieService {
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    @Autowired
    public TDMBMovieService(RestTemplateBuilder restTemplateBuilder, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}")String baseUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    private TDMBMovieResponse searchMovies(String query, int page, String language) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/search/movie")
                .queryParam("include_adult", true)
                .queryParam("language", language)
                .queryParam("page", page)
                .queryParam("query", query);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                TDMBMovieResponse.class
        ).getBody();
    }

    @Override
    public MovieResponseView searchMovie(String query, int page, String language) {
        TDMBMovieResponse response = searchMovies(query, page, language);
        MovieResponseView movieResponseView = new MovieResponseView();
        movieResponseView.setPage(page);
        movieResponseView.setTotalPages(response.getTotal_pages());
        movieResponseView.setTotalResults(response.getTotal_results());

        List<MovieResponseView.Movie> movieResults = response.getResults().stream().map((e)->{
            MovieResponseView.Movie movie = new MovieResponseView.Movie();
            movie.setId(e.getId());
            movie.setTitle(e.getTitle());
            movie.setReleaseDate(e.getRelease_date());
            movie.setOverview(e.getOverview());
            String imageUrl = String.format("https://image.tmdb.org/t/p/w500/%s", e.getPoster_path());
            movie.setPosterUrl(imageUrl);

//            try {
//                Method getMovieMethod = SearchMultimediaController.class.getMethod("getMovie", Integer.class);
//                Link link = linkTo(getMovieMethod, movie.getId()).withRel(LinkRelation.of("href"));
//                movie.setHref(link.getHref());
//            } catch (NoSuchMethodException ex) {
//                throw new RuntimeException(ex);
//            }
            return movie;
        }).toList();

        movieResponseView.setResults(movieResults);
        movieResponseView.setProvider("tmdb");
        return movieResponseView;
    }

    @Override
    public MovieResponseView.Movie getMovie(int movieId) {
        return null;
    }
}