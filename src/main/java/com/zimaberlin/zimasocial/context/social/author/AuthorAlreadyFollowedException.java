package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class AuthorAlreadyFollowedException extends ConflictException {
    public AuthorAlreadyFollowedException(AuthorId authorId) {
        super("author_already_followed", String.format("Author with given id: %s is already followed", authorId.getValue()));
    }

    public AuthorAlreadyFollowedException(String slug) {
        super("author_already_followed", String.format("Author with slug: %s is already followed", slug));
    }
}
