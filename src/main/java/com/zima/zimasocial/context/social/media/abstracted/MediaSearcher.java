package com.zima.zimasocial.context.social.media.abstracted;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zima.zimasocial.context.social.media.entity.MediaItem;

public interface MediaSearcher {
    MediaItem get(String id) throws JsonProcessingException;
}
