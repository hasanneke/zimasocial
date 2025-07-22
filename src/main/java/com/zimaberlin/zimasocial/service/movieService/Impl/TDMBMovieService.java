//package com.zimaberlin.zimasocial.service.movieService.Impl;
//
//import com.zimaberlin.zimasocial.service.movieService.domain.MovieResponseView;
//import com.zimaberlin.zimasocial.service.movieService.domain.SearchMovieService;
//import com.zimaberlin.zimasocial.service.movieService.domain.TVShowResponseView;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class TDMBMovieService implements SearchMovieService {
//    private final RestTemplate restTemplate;
//    private final String apiKey;
//    private final String baseUrl;
//
//    @Autowired
//    public TDMBMovieService(RestTemplateBuilder restTemplateBuilder, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}")String baseUrl) {
//        this.restTemplate = restTemplateBuilder.build();
//        this.apiKey = apiKey;
//        this.baseUrl = baseUrl;
//    }
//    @Override
//    public MovieResponseView searchMovie(String query, int page, String language) {
//        TDMBMovieResponse response = searchMovies(query, page, language);
//        MovieResponseView movieResponseView = new MovieResponseView();
//        movieResponseView.setPage(page);
//        movieResponseView.setTotalPages(response.getTotal_pages());
//        movieResponseView.setTotalResults(response.getTotal_results());
//
//        List<MovieResponseView.Movie> movieResults = response.getResults().stream().map(TDMBMovieService::convertTDMBMovieToDomain).toList();
//
//        movieResponseView.setResults(movieResults);
//        movieResponseView.setProvider("tmdb");
//        return movieResponseView;
//    }
//
//    @Override
//    public MovieResponseView.Movie getMovie(Integer movieId, String language) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        HttpEntity entity = new HttpEntity<>(headers);
//
//        UriComponentsBuilder uriComponentsBuilder =
//                UriComponentsBuilder
//                        .fromHttpUrl(String.format("%s/movie/%s", baseUrl, movieId.toString()))
//                        .queryParam("language", language);
//
//        ResponseEntity<TDMBMovieResponse.MovieTvResult> result = restTemplate.exchange(
//                uriComponentsBuilder.toUriString(),
//                HttpMethod.GET,
//                entity,
//                TDMBMovieResponse.MovieTvResult.class
//        );
//
//        return convertTDMBMovieToDomain(Objects.requireNonNull(result.getBody()));
//    }
//
//    private TDMBMovieResponse searchMovies(String query, int page, String language) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        UriComponentsBuilder movieUri = UriComponentsBuilder
//                .fromHttpUrl(baseUrl + "/search/movie")
//                .queryParam("include_adult", false)
//                .queryParam("language", language)
//                .queryParam("page", page)
//                .queryParam("query", query);
//
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        TDMBMovieResponse movieResponse = restTemplate.exchange(
//                movieUri.toUriString(),
//                HttpMethod.GET,
//                entity,
//                TDMBMovieResponse.class
//        ).getBody();
//
//        assert movieResponse != null;
//
//        return movieResponse;
//    }
//
//    private static MovieResponseView.Movie convertTDMBMovieToDomain(TDMBMovieResponse.MovieTvResult e) {
//        MovieResponseView.Movie movie = new MovieResponseView.Movie();
//        movie.setId(e.getId());
//        movie.setTitle(e.getTitle());
//        movie.setBackdropUrl(String.format("https://image.tmdb.org/t/p/w500/%s", e.getBackdrop_path()));
//        movie.setReleaseDate(e.getRelease_date());
//        movie.setOverview(e.getOverview());
//        movie.setVoteCount(e.getVote_count());
//        movie.setVoteAverage(e.getVote_average());
//        movie.setAdult(e.isAdult());
//        movie.setPopularity(e.getPopularity());
//        movie.setOriginalLanguage(e.getOriginal_language());
//        movie.setOriginalTitle(e.getOriginal_title());
//        String imageUrl = String.format("https://image.tmdb.org/t/p/w500/%s", e.getPoster_path());
//        movie.setPosterUrl(imageUrl);
//        return movie;
//    }
//}