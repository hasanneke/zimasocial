package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.service.movieService.domain.MovieResponseView;

import java.util.List;

public interface MovieSearcher {
    MovieResponseView.Movie getMovie(Integer movieId, MovieMediaType type, String language);

    List<SearchMovieMediaItem> search(String query, int page, String language);
}
