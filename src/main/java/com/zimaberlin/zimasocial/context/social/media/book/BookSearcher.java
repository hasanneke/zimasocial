package com.zimaberlin.zimasocial.context.social.media.book;

import java.util.List;
import java.util.Optional;

public interface BookSearcher {
    List<SearchBookMediaItem> search(String query);
    Optional<BookMedia> getBook(String id);
}
