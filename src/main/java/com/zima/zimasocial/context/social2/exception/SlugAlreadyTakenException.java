package com.zima.zimasocial.context.social2.exception;

import com.zima.zimasocial.exception.ConflictException;

public class SlugAlreadyTakenException extends ConflictException {
    public SlugAlreadyTakenException(String slug) {
        super("slug_already_taken", String.format("Slug: %s is already taken", slug));
    }
}
