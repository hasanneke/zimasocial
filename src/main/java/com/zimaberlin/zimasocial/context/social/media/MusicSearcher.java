package com.zimaberlin.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;

public interface MusicSearcher {
    MediaItem get(String id) throws JsonProcessingException;
}
