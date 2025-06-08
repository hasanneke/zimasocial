package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class BioLengthExceededException extends BadRequestException {
    public BioLengthExceededException(String message) {
        super(message);
    }
    public BioLengthExceededException(){
        super("bio_length_exceeded", "Bio length can be maximum 128 character");
    }
}
