package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class BookNotFoundException extends DataNotFoundException {
    public BookNotFoundException() {
        super("book_not_found", "Book not found");
    }
}
