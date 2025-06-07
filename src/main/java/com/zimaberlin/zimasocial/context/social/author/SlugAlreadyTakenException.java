package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class SlugAlreadyTakenException extends ConflictException {
    public SlugAlreadyTakenException(String slug) {
        super("slug_already_taken", String.format("Slug: %s is already taken", slug));
    }
}
