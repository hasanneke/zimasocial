package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.media.MovieMediaType;
import com.zimaberlin.zimasocial.context.social.media.SearchMovieMediaItem;
import com.zimaberlin.zimasocial.context.social.media.MovieSearcher;
import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import com.zimaberlin.zimasocial.service.movieService.domain.MovieResponseView;
import com.zimaberlin.zimasocial.service.movieService.domain.SearchMovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieSearcherAdapter implements MovieSearcher {
    private final SearchMovieService searchMovieService;

    public MovieSearcherAdapter(SearchMovieService searchMovieService) {
        this.searchMovieService = searchMovieService;
    }

    @Override
    public SearchMovieMediaItem getMovieSearchItem(Integer movieId, MovieMediaType type, String language) {
        MovieResponseView.Movie movie = searchMovieService.getMovie(movieId, type, language);
        return mapToMovie(movie);
    }

    @Override
    public MovieResponseView.Movie getMovie(Integer movieId, MovieMediaType type, String language) {
        return searchMovieService.getMovie(movieId, type, language);
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
       MovieResponseView movieResponseView = searchMovieService.searchMovie(query, page, language);
       return movieResponseView.getResults().stream().map(MovieSearcherAdapter::mapToMovie).toList();
    }
}
