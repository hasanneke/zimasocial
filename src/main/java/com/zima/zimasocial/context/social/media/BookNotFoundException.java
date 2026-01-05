package com.zima.zimasocial.context.social.media;

import com.zima.zimasocial.exception.DataNotFoundException;

public class BookNotFoundException extends DataNotFoundException {
    public BookNotFoundException() {
        super("book_not_found", "Book not found");
    }
}
