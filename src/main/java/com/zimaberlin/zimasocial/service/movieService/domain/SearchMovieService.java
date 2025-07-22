package com.zimaberlin.zimasocial.service.movieService.domain;

import com.zimaberlin.zimasocial.context.social.media.MovieMediaType;

public interface SearchMovieService {
    MovieResponseView searchMovie(String query, int page, String language);
    MovieResponseView.Movie getMovie(Integer id, MovieMediaType type, String language);
}
