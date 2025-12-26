package com.zimaberlin.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;

public interface MovieSearcher {
    MediaItem getMovie(Integer movieId, MovieMediaType type, String language) throws JsonProcessingException;
}
