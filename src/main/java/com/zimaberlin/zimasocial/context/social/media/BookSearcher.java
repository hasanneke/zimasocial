package com.zimaberlin.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import com.zimaberlin.zimasocial.context.social.media.book.SearchBookMediaItem;

import java.util.List;

public interface BookSearcher {
    List<SearchBookMediaItem> search(String query);
    MediaItem getBook(String id) throws JsonProcessingException;
}
