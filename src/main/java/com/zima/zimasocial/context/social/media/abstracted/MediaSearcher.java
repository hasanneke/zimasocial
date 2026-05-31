package com.zima.zimasocial.context.social.media.abstracted;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zima.zimasocial.context.social.media.entity.Media;

public interface MediaSearcher {
    Media get(String id) throws JsonProcessingException;
}
