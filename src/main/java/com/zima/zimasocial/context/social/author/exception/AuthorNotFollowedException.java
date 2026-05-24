package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.exception.DataNotFoundException;

public class AuthorNotFollowedException extends DataNotFoundException {
    public AuthorNotFollowedException(AuthorDomainId authorId) {
        super("author_not_followed", String.format("Author with given id: %d is not followed", authorId.getValue()));
    }

    public AuthorNotFollowedException(String slug) {
        super("author_not_followed", String.format("Author with given slug: %s is not followed", slug));
    }
}
