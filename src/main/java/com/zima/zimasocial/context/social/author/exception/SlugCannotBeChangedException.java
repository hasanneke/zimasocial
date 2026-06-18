package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.shared.exception.BadRequestException;

public class SlugCannotBeChangedException extends BadRequestException {
    public SlugCannotBeChangedException(String message) {
        super("slug_not_changed", message);
    }
}
