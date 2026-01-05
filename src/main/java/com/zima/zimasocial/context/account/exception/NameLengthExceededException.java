package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.exception.BadRequestException;

public class NameLengthExceededException extends BadRequestException {
    public NameLengthExceededException(String message) {
        super(message);
    }
    public NameLengthExceededException(){
        super("name_length_exceede", "Name length can be maximum 32 character");
    }
}
