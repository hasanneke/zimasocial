package com.zima.zimasocial.context.social.media;

import com.zima.zimasocial.exception.BadRequestException;

public class PostTypeMediaMismatchException extends BadRequestException {
    public PostTypeMediaMismatchException() {
        super("post_type_media_mismatch", "You cannot set movie media to non-movie type post");
    }
}
