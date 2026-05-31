package com.zima.zimasocial.context.social.playlist.api.dto;

import com.zima.zimasocial.context.social.playlist.infastructure.Playlist;
import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlaylistDTO {
    private UUID id;
    private String name;
    private MediaType type;
    private String slug;
    private Long count;
    public PlaylistDTO(Playlist playlist, String slug) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.type = playlist.getType();
        this.slug = slug;
    }


    public PlaylistDTO(UUID id, String name, MediaType type, Long count) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.count = count;
    }
}
