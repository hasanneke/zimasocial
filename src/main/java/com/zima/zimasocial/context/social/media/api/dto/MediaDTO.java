package com.zima.zimasocial.context.social.media.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.zima.zimasocial.context.social.media.entity.MediaItem;
import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class MediaDTO {
    private UUID id;
    private String provider;
    private JsonNode content;
    private MediaType type;

    public MediaDTO(MediaItem mediaItem) {
        this.id = mediaItem.getId();
        this.provider = mediaItem.getProvider();
        this.type = mediaItem.getType();
    }
}
