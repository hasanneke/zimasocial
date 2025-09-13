package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class SlugCannotBeChangedException extends BadRequestException {
    public SlugCannotBeChangedException(String message) {
        super("slug_not_changed", message);
    }
}
