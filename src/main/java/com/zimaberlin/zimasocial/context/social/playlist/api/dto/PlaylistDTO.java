package com.zimaberlin.zimasocial.context.social.playlist.api.dto;

import com.zimaberlin.zimasocial.context.social.playlist.entity.Playlist;
import com.zimaberlin.zimasocial.entity.MediaType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlaylistDTO {
    private UUID id;
    private String name;
    private MediaType type;
    private String slug;
    public PlaylistDTO(Playlist playlist, String slug) {
        this.id = playlist.getId().value();
        this.name = playlist.getName();
        this.type = playlist.getType();
        this.slug = slug;
    }
}
