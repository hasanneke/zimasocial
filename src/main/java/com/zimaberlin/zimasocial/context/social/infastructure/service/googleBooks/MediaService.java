package com.zimaberlin.zimasocial.context.social.infastructure.service.googleBooks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.infastructure.MediaProviders;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import com.zimaberlin.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zimaberlin.zimasocial.context.social.media.BookSearcher;
import com.zimaberlin.zimasocial.context.social.media.MovieSearcher;
import com.zimaberlin.zimasocial.context.social.media.MusicSearcher;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.exception.BadRequestException;
import com.zimaberlin.zimasocial.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final BookSearcher bookSearcher;
    private final MusicSearcher musicSearcher;
    private final MovieSearcher movieSearcher;
    private final MediaItemJpaRepository mediaItemJpaRepository;
    public UUID getId(String resourceId, PostType type, MovieMediaType movieMediaType) throws JsonProcessingException {
        Assert.notNull(type, "type cannot be null");
        Assert.isTrue(type != PostType.any, "type cannot be PostType.any");
        Optional<UUID> mediaId = mediaItemJpaRepository.findIdByResourceIdAndProvider(resourceId, MediaProviders.findByPostType(type).getName());
        if(mediaId.isPresent()){
            return mediaId.get();
        }
        MediaItem mediaItem = null;
        switch (type){
            case PostType.music -> mediaItem = musicSearcher.get(resourceId);
            case PostType.movie -> mediaItem = movieSearcher.getMovie(Integer.valueOf(resourceId), movieMediaType, "tr");
            case PostType.book -> mediaItem = bookSearcher.getBook(resourceId);
            default -> throw new BadRequestException("Type is not valid");
        }
        if(mediaItem == null){
            throw new DataNotFoundException("Media not found");
        }
        MediaItem savedMedia = mediaItemJpaRepository.save(mediaItem);
        return savedMedia.getId();
    }

    public String get(UUID mediaId) throws JsonProcessingException {
        MediaItem mediaItem = mediaItemJpaRepository.findById(mediaId).orElseThrow(() -> new DataNotFoundException("media_not_found"));
        return mediaItem.getContent();
    }
}
