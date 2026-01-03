package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class MediaNotFoundException extends DataNotFoundException {
    public MediaNotFoundException() {
        super("media_not_found", "Media not found");
    }
}
