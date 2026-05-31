package com.zima.zimasocial.context.social.playlist.api.dto;

import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlaylistItemDTO {
    private final UUID mediaId;
    private final String provider;
    private final MediaType type;
    public PlaylistItemDTO(Media item) {
        this.type = item.getType();
        this.mediaId = item.getId().getValue();
        this.provider = item.getProvider();
    }
}
