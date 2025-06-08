package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class NameLengthExceededException extends BadRequestException {
    public NameLengthExceededException(String message) {
        super(message);
    }
    public NameLengthExceededException(){
        super("name_length_exceede", "Name length can be maximum 32 character");
    }
}
