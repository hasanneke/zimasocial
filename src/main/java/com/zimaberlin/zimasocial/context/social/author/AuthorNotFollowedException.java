package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class AuthorNotFollowedException extends DataNotFoundException {
    public AuthorNotFollowedException(AuthorId authorId) {
        super("author_not_followed", String.format("Author with given id: %d is not followed", authorId.getValue()));
    }

    public AuthorNotFollowedException(String slug) {
        super("author_not_followed", String.format("Author with given slug: %s is not followed", slug));
    }
}
