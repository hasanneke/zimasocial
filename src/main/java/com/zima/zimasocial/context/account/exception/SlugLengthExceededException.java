package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.BadRequestException;

public class SlugLengthExceededException extends BadRequestException {
    public SlugLengthExceededException(String message) {
        super(message);
    }
    public SlugLengthExceededException(){
        super("slug_length_exceeded", "Slug length can be maximum 32 character");
    }
}