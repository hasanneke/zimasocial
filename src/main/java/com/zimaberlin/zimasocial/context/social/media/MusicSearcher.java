package com.zimaberlin.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface MusicSearcher {
    UUID get(String id) throws JsonProcessingException;
}
