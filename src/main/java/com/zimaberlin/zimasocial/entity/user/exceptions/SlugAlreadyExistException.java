package com.zimaberlin.zimasocial.entity.user.exceptions;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class SlugAlreadyExistException extends ConflictException {
    public SlugAlreadyExistException() {
        super("slug_already_exists", "Slug is already used by another user");
    }
}
