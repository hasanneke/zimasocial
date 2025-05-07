package com.zimaberlin.zimasocial.service.posts.exception;

import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends ResourceNotFoundException {
    public PostNotFoundException() {
        super("Post not found");
    }
}
