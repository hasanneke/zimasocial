package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.exception.ConflictException;

public class AuthorNotBlockedException extends ConflictException {
    public AuthorNotBlockedException(Long authorId) {
        super("author_not_blocked", String.format("Author with given id: %d not blocked", authorId));
    }

    public AuthorNotBlockedException(String slug) {
        super("author_not_blocked", String.format("Author %s not blocked", slug));
    }
}
