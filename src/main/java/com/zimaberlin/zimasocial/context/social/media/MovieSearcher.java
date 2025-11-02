package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.context.social.media.movie.SearchMovieMediaItem;

import java.util.List;

public interface MovieSearcher {
    SearchMovieMediaItem getMovieSearchItem(Integer movieId, MovieMediaType type, String language);
    MovieMedia getMovie(Integer movieId, MovieMediaType type, String language);

    List<SearchMovieMediaItem> search(String query, int page, String language);
}
