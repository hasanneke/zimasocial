package com.zimaberlin.zimasocial.context.social.author.exception;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class AuthorNotFoundException extends DataNotFoundException {
    public AuthorNotFoundException() {
        super("author_not_found", "Author not found ");
    }
    public AuthorNotFoundException(Long authorId) {
        super("author_not_found", String.format("Author not found with given id: %d", authorId));
    }
    public AuthorNotFoundException(String slug) {
        super("author_not_found", String.format("Author not found with slug id: %s", slug));
    }
}
