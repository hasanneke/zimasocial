package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.shared.exception.ConflictException;

public class AuthorAlreadyBlocked extends ConflictException {
    public AuthorAlreadyBlocked(AuthorId authorId) {
        super("author_already_blocked", String.format("Author with given id: %d is already blocked", authorId.getValue()));
    }

    public AuthorAlreadyBlocked(String slug) {
        super("author_already_blocked", String.format("Author with given username: %s is already blocked", slug));
    }
}
