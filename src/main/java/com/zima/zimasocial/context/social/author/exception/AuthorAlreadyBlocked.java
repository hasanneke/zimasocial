package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.exception.ConflictException;

public class AuthorAlreadyBlocked extends ConflictException {
    public AuthorAlreadyBlocked(AuthorDomainId authorId) {
        super("author_already_blocked", String.format("Author with given id: %d is already blocked", authorId.getValue()));
    }
}
