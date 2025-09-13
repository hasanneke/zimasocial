package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.infastructure.adapter.MediaDBAdapter;
import com.zimaberlin.zimasocial.context.social.media.MediaCollection;
import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.entity.media.*;
import com.zimaberlin.zimasocial.repository.MediaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MediaDBCollection implements MediaCollection {
    private final MediaJpaRepository mediaJpaRepository;
    private final MediaDBAdapter mediaDBAdapter;

    @Override
    public Optional<MovieMedia> findMovieById(UUID id) {
        MediaJpa mediaJpa = mediaJpaRepository.findById(id).orElse(null);
        return Optional.ofNullable(mediaDBAdapter.convertToMedia(mediaJpa));
    }

    @Override
    public Optional<BookMedia> findBookById(UUID id) {
        MediaJpa bookMedia = mediaJpaRepository.findById(id).orElse(null);
        return Optional.ofNullable(mediaDBAdapter.convertToBookMedia(bookMedia));
    }

    @Override
    public UUID nextId() {
        return mediaJpaRepository.nextUUID();
    }
}
