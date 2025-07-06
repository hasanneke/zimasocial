package com.zimaberlin.zimasocial.context.social.media;

import java.util.List;

public interface MovieSearcher {
    SearchMovieMediaItem getMovie(Integer movieId, String language);
    List<SearchMovieMediaItem> search(String query, int page, String language);
}
