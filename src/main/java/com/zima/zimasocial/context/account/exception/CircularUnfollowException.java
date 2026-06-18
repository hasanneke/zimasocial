package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.BadRequestException;

public class CircularUnfollowException extends BadRequestException {
    final String code;
    public CircularUnfollowException() {
        super("Circular unfollow exception");
        this.code = "circular_unfollow_exception";
    }
}