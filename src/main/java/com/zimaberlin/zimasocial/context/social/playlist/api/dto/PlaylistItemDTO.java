package com.zimaberlin.zimasocial.context.social.playlist.api.dto;

import com.zimaberlin.zimasocial.context.social.media.infastructure.MediaItem;
import com.zimaberlin.zimasocial.entity.MediaType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlaylistItemDTO {
    private final UUID mediaId;
    private final String provider;
    private final MediaType type;
    public PlaylistItemDTO(MediaItem mediaItem) {
        this.type = mediaItem.getType();
        this.mediaId = mediaItem.getId();
        this.provider = mediaItem.getProvider();
    }
}
