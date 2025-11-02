package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class AuthorAlreadyBlocked extends ConflictException {
    public AuthorAlreadyBlocked(AuthorId authorId) {
        super("author_already_blocked", String.format("Author with given id: %d is already blocked", authorId.getValue()));
    }
}
