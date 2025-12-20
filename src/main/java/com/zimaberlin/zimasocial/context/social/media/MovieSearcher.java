package com.zimaberlin.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;

import java.util.UUID;

public interface MovieSearcher {
    UUID getMovie(Integer movieId, MovieMediaType type, String language) throws JsonProcessingException;
}
