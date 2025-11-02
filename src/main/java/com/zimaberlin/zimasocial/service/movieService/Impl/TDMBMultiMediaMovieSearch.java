//package com.zimaberlin.zimasocial.service.movieService.Impl;
//
//import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
//import com.zimaberlin.zimasocial.context.social.media.infastructure.movie.MovieResponseView;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class TDMBMultiMediaMovieSearch implements SearchMovieService {
//    private final RestTemplate restTemplate;
//    private final String apiKey;
//    private final String baseUrl;
//
//    @Autowired
//    public TDMBMultiMediaMovieSearch(RestTemplateBuilder restTemplateBuilder, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}")String baseUrl) {
//        this.restTemplate = restTemplateBuilder.build();
//        this.apiKey = apiKey;
//        this.baseUrl = baseUrl;
//    }
//
//    @Override
//    public MovieResponseView.Movie getMovie(Integer id, MovieMediaType type, String language) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        HttpEntity entity = new HttpEntity<>(headers);
//        assert type != null;
//        if (type.equals(MovieMediaType.movie)) {
//            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/movie/%s", baseUrl, id)).queryParam("language", language);
//            ResponseEntity<TDMBMultiMediaTVMovieSearchResponse.MovieItem> result = restTemplate.exchange(
//                    uriComponentsBuilder.toUriString(),
//                    HttpMethod.GET,
//                    entity,
//                    TDMBMultiMediaTVMovieSearchResponse.MovieItem.class
//            );
//            return MovieResponseViewAdapter.convertTDMBMovieToDomain(Objects.requireNonNull(result.getBody()));
//        }else {
//            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/tv/%s", baseUrl, id)).queryParam("language", language);
//            ResponseEntity<TDMBMultiMediaTVMovieSearchResponse.TvShow> result = restTemplate.exchange(
//                    uriComponentsBuilder.toUriString(),
//                    HttpMethod.GET,
//                    entity,
//                    TDMBMultiMediaTVMovieSearchResponse.TvShow.class
//            );
//            return MovieResponseViewAdapter.convertTDMBMovieToDomain(Objects.requireNonNull(result.getBody()));
//        }
//    }
//
//    @Override
//    public MovieResponseView searchMovie(String query, int page, String language) {
//        TDMBMultiMediaTVMovieSearchResponse response = searchMovies(query, page, language);
//        MovieResponseView movieResponseView = new MovieResponseView();
//        movieResponseView.setPage(page);
//        movieResponseView.setTotalPages(response.getTotal_pages());
//        movieResponseView.setTotalResults(response.getTotal_results());
//
//        List<MovieResponseView.Movie> movieResults =
//                response.getResults().stream().filter(e->!e.getMedia_type().equals("person"))
//                        .map(MovieResponseViewAdapter::convertTDMBMovieToDomain).toList();
//
//        movieResponseView.setResults(movieResults);
//        movieResponseView.setProvider("tmdb");
//        return movieResponseView;
//    }
//
//    private TDMBMultiMediaTVMovieSearchResponse searchMovies(String query, int page, String language) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        UriComponentsBuilder movieUri = UriComponentsBuilder
//                .fromHttpUrl(baseUrl + "/search/multi")
//                .queryParam("include_adult", false)
//                .queryParam("language", language)
//                .queryParam("page", page)
//                .queryParam("query", query);
//
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        TDMBMultiMediaTVMovieSearchResponse movieResponse = restTemplate.exchange(
//                movieUri.toUriString(),
//                HttpMethod.GET,
//                entity,
//                TDMBMultiMediaTVMovieSearchResponse.class
//        ).getBody();
//
//        assert movieResponse != null;
//
//        return movieResponse;
//    }
//}
