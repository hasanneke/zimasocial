package com.zima.zimasocial.service.posts.exception;

import com.zima.zimasocial.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends DataNotFoundException {
    public PostNotFoundException() {
        super("Post not found");
    }
}
