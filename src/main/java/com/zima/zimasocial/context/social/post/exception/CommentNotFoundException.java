package com.zima.zimasocial.context.social.post.exception;

import com.zima.zimasocial.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends DataNotFoundException {
    public CommentNotFoundException() {
        super("Comment not found");
    }
}
