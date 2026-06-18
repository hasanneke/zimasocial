package com.zima.zimasocial.context.social.media.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.value.MediaType;
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

    public MediaDTO(Media media) {
        this.id = media.getId().getValue();
        this.provider = media.getProvider();
        this.type = media.getType();
    }
}
