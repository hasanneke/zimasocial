package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;

import java.util.Optional;
import java.util.UUID;

public interface MediaCollection {
    Optional<MovieMedia> findMovieById(UUID id);
    Optional<BookMedia> findBookById(UUID id);
    UUID nextId();
}
