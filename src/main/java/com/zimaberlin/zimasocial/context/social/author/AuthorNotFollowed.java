package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class AuthorNotFollowed extends DataNotFoundException {
    public AuthorNotFollowed(Long authorId) {
        super("author_not_followed", String.format("Author with given id: %d is not followed", authorId));
    }
}
