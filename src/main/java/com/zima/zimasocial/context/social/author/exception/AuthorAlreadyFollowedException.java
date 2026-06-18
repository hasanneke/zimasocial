package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.shared.exception.ConflictException;

public class AuthorAlreadyFollowedException extends ConflictException {
    public AuthorAlreadyFollowedException(AuthorId authorId) {
        super("author_already_followed", String.format("Author with given id: %s is already followed", authorId.getValue()));
    }

    public AuthorAlreadyFollowedException(String slug) {
        super("author_already_followed", String.format("Author with slug: %s is already followed", slug));
    }
}
