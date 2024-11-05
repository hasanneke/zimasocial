package com.zimaberlin.zimasocial.service.movieService.domain;

public interface SearchMovieService {
    MovieResponseView searchMovie(String query, int page, String language);
    MovieResponseView.Movie getMovie(int id);
}
