package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class SlugAlreadyExistException extends ConflictException {
    public SlugAlreadyExistException() {
        super("slug_already_exists", "Slug is already used by another user");
    }
}
