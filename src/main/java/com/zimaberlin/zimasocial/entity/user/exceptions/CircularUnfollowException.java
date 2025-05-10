package com.zimaberlin.zimasocial.entity.user.exceptions;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class CircularUnfollowException extends BadRequestException {
    final String code;
    public CircularUnfollowException() {
        super("Circular unfollow exception");
        this.code = "circular_unfollow_exception";
    }
}