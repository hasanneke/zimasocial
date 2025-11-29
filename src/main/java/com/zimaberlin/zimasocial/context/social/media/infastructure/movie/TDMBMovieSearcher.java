package com.zimaberlin.zimasocial.context.social.media.infastructure.movie;

import com.zimaberlin.zimasocial.context.social.media.*;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.context.social.media.MovieSearcher;
import com.zimaberlin.zimasocial.context.social.media.movie.SearchMovieMediaItem;
import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import com.zimaberlin.zimasocial.service.movieService.Impl.MovieResponseViewAdapter;
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
import java.util.List;
import java.util.Objects;

@Service
public class TDMBMovieSearcher implements MovieSearcher {
    private final RestTemplate restTemplate;
    private final MediaCollection mediaCollection;
    private final String apiKey;
    private final String baseUrl;

    @Autowired
    public TDMBMovieSearcher(RestTemplateBuilder restTemplateBuilder, MediaCollection mediaCollection, @Value("${mediaprovider.movie.apikey}") String apiKey, @Value("${mediaprovider.movie.baseurl}")String baseUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.mediaCollection = mediaCollection;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    @Override
    public SearchMovieMediaItem getMovieSearchItem(Integer movieId, MovieMediaType type, String language) {
        MovieResponseView.Movie movie = getTDMBMovieResponse(movieId, type, language);
        return mapToMovie(movie);
    }

    @Override
    public MovieMedia getMovie(Integer movieId, MovieMediaType type, String language) {
        return createMovieMediaObject(getTDMBMovie(movieId, type, language));
    }

    public MovieResponseView.Movie getTDMBMovieResponse(Integer movieId, MovieMediaType type, String language) {
        return getTDMBMovie(movieId, type, language);
    }

    private static SearchMovieMediaItem mapToMovie(MovieResponseView.Movie movie) {
        return SearchMovieMediaItem.builder()
                .backdropUrl(movie.getBackdropUrl())
                .resourceId(movie.getId())
                .name(movie.getTitle())
                .summary(movie.getOverview())
                .voteAverage(movie.getVoteAverage())
                .releaseDate(movie.getReleaseDate())
                .voteCount(movie.getVoteCount())
                .posterUrl(movie.getPosterUrl())
                .originalLanguage(movie.getOriginalLanguage())
                .description(movie.getOverview())
                .movieProvider(MovieProvider.TMDB)
                .type(movie.getType())
                .build();
    }

    @Override
    public List<SearchMovieMediaItem> search(String query, int page, String language) {
       MovieResponseView movieResponseView = searchTDMBMovie(query, page, language);
       return movieResponseView.getResults().stream().map(TDMBMovieSearcher::mapToMovie).toList();
    }

    public MovieResponseView.Movie getTDMBMovie(Integer id, MovieMediaType type, String language) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity entity = new HttpEntity<>(headers);
        assert type != null;
        if (type.equals(MovieMediaType.movie)) {
            URI uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/movie/%s", baseUrl, id))
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
            return MovieResponseViewAdapter.convertTDMBMovieToDomain(Objects.requireNonNull(result.getBody()));
        }else {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(String.format("%s/tv/%s", baseUrl, id)).queryParam("language", language);
            ResponseEntity<TDMBMultiMediaTVMovieSearchResponse.TvShow> result = restTemplate.exchange(
                    uriComponentsBuilder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    TDMBMultiMediaTVMovieSearchResponse.TvShow.class
            );
            return MovieResponseViewAdapter.convertTDMBMovieToDomain(Objects.requireNonNull(result.getBody()));
        }
    }

    public MovieResponseView searchTDMBMovie(String query, int page, String language) {
        TDMBMultiMediaTVMovieSearchResponse response = searchMovies(query, page, language);
        MovieResponseView movieResponseView = new MovieResponseView();
        movieResponseView.setPage(page);
        movieResponseView.setTotalPages(response.getTotal_pages());
        movieResponseView.setTotalResults(response.getTotal_results());

        List<MovieResponseView.Movie> movieResults =
                response.getResults().stream().filter(e->!e.getMedia_type().equals("person"))
                        .map(MovieResponseViewAdapter::convertTDMBMovieToDomain).toList();

        movieResponseView.setResults(movieResults);
        movieResponseView.setProvider("tmdb");
        return movieResponseView;
    }

    private TDMBMultiMediaTVMovieSearchResponse searchMovies(String query, int page, String language) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        UriComponentsBuilder movieUri = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/search/multi")
                .queryParam("include_adult", false)
                .queryParam("language", language)
                .queryParam("page", page)
                .queryParam("query", query);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        TDMBMultiMediaTVMovieSearchResponse movieResponse = restTemplate.exchange(
                movieUri.toUriString(),
                HttpMethod.GET,
                entity,
                TDMBMultiMediaTVMovieSearchResponse.class
        ).getBody();

        assert movieResponse != null;

        return movieResponse;
    }

    private MovieMedia createMovieMediaObject(MovieResponseView.Movie movieRes){
        return MovieMedia.builder()
                .id(mediaCollection.nextId())
                .resourceId(Integer.valueOf(movieRes.getId()).toString())
                .movieProvider(movieRes.getProvider())
                .originalLanguage(movieRes.getOriginalLanguage())
                .name(movieRes.getTitle())
                .description(movieRes.getOverview())
                .posterUrl(movieRes.getPosterUrl())
                .backdropUrl(movieRes.getBackdropUrl())
                .releaseDate(movieRes.getReleaseDate())
                .summary(movieRes.getOverview())
                .voteAverage(movieRes.getVoteAverage())
                .voteCount(movieRes.getVoteCount())
                .type(movieRes.getType())
                .numberOfEpisodes(movieRes.getNumberOfEpisodes())
                .numberOfSeasons(movieRes.getNumberOfSeasons())
                .build();
    }
}
