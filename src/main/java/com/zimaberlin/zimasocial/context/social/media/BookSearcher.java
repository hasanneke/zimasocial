package com.zimaberlin.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.media.book.SearchBookMediaItem;

import java.util.List;
import java.util.UUID;

public interface BookSearcher {
    List<SearchBookMediaItem> search(String query);
    UUID getBook(String id) throws JsonProcessingException;
}
