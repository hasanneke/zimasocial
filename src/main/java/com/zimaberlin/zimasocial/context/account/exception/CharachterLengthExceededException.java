package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class CharachterLengthExceededException extends BadRequestException {
    public CharachterLengthExceededException(String message) {
        super(message);
    }
    public CharachterLengthExceededException(int length){
        super("chrachter_length_exceeded", "Bio length can be maximum %d character".formatted(length));
    }
}
