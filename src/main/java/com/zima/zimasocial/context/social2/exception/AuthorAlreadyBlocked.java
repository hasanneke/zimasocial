package com.zima.zimasocial.context.social2.exception;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.exception.ConflictException;

public class AuthorAlreadyBlocked extends ConflictException {
    public AuthorAlreadyBlocked(AuthorId authorId) {
        super("author_already_blocked", String.format("Author with given id: %d is already blocked", authorId.getValue()));
    }

    public AuthorAlreadyBlocked(String slug) {
        super("author_already_blocked", String.format("Author with given username: %s is already blocked", slug));
    }
}
