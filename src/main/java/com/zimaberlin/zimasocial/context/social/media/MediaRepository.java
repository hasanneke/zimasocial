package com.zimaberlin.zimasocial.context.social.media;

import java.util.Optional;
import java.util.UUID;

public interface MediaRepository {
    MovieMedia save(MovieMedia movie);
    Optional<MovieMedia> findMovieById(UUID id);
}
