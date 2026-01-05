package com.zima.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zima.zimasocial.context.social.media.infastructure.MediaItem;

public interface MediaSearcher {
    MediaItem get(String id) throws JsonProcessingException;
}
