package com.zimaberlin.zimasocial.context.social.infastructure.service.googleBooks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.media.BookSearcher;
import com.zimaberlin.zimasocial.context.social.media.MovieSearcher;
import com.zimaberlin.zimasocial.context.social.media.MusicSearcher;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final BookSearcher bookSearcher;
    private final MusicSearcher musicSearcher;
    private final MovieSearcher movieSearcher;
    public UUID get(String resourceId, PostType type, MovieMediaType movieMediaType) throws JsonProcessingException {
        Assert.notNull(type, "type cannot be null");
        Assert.isTrue(type != PostType.any, "type cannot be PostType.any");
        switch (type){
            case PostType.music -> {
                return musicSearcher.get(resourceId);
            }
            case PostType.movie -> {
                return movieSearcher.getMovie(Integer.valueOf(resourceId), movieMediaType, "tr");
            }
            case PostType.book -> {
                return bookSearcher.getBook(resourceId);
            }
            default -> throw new BadRequestException("Type is not valid");
        }
    }
}
