package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class SlugLengthExceededException extends BadRequestException {
    public SlugLengthExceededException(String message) {
        super(message);
    }
    public SlugLengthExceededException(){
        super("name_length_exceede", "Name length can be maximum 32 character");
    }
}