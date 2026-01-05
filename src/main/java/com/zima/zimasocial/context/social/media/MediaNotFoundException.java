package com.zima.zimasocial.context.social.media;

import com.zima.zimasocial.exception.DataNotFoundException;

public class MediaNotFoundException extends DataNotFoundException {
    public MediaNotFoundException() {
        super("media_not_found", "Media not found");
    }
}
