package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class MovieNotFoundException extends DataNotFoundException {
    public MovieNotFoundException() {
        super("movie_not_found", "Movie not found");
    }
}
