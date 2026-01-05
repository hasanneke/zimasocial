package com.zima.zimasocial.context.social.media;

import com.zima.zimasocial.exception.DataNotFoundException;

public class MovieNotFoundException extends DataNotFoundException {
    public MovieNotFoundException() {
        super("movie_not_found", "Movie not found");
    }
}
