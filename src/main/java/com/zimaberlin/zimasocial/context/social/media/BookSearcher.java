package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.book.SearchBookMediaItem;

import java.util.List;
import java.util.Optional;

public interface BookSearcher {
    List<SearchBookMediaItem> search(String query);
    Optional<BookMedia> getBook(String id);
}
