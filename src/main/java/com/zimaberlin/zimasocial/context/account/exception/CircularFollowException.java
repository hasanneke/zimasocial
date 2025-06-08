package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class CircularFollowException extends BadRequestException {
    final String code;
    public CircularFollowException() {
        super("Circular follow exception");
        this.code = "circular_follow_exception";
    }
}
