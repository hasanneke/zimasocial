package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class AuthorNotBlockedException extends ConflictException {
    public AuthorNotBlockedException(Long authorId) {
        super("author_not_blocked", String.format("Author with given id: %d not blocked", authorId));
    }
}
