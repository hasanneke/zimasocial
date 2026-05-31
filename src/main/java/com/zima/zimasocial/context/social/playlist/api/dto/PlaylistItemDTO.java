package com.zima.zimasocial.context.social.playlist.api.dto;

import com.zima.zimasocial.context.social.media.entity.MediaItem;
import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlaylistItemDTO {
    private final UUID mediaId;
    private final String provider;
    private final MediaType type;
    public PlaylistItemDTO(MediaItem item) {
        this.type = item.getType();
        this.mediaId = item.getId();
        this.provider = item.getProvider();
    }
}
