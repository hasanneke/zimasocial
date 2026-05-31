package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.exception.ConflictException;

public class SlugAlreadyTakenException extends ConflictException {
    public SlugAlreadyTakenException(String slug) {
        super("slug_already_taken", String.format("Slug: %s is already taken", slug));
    }
}
