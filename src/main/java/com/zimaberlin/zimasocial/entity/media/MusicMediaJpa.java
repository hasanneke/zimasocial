package com.zimaberlin.zimasocial.entity.media;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class MusicMediaJpa {
    @Column(name = "song_resource_id", length = 128)
    private String id;
    @Column(name = "singer_name", length = 64)
    private String name;
    @Column(name = "song_duration")
    private Integer duration;
    @Column(name = "album_cover_url", length = 512)
    private String albumCoverUrl;
    @Column(name = "spotify_short_play_url", length = 512)
    private String spotifyShortPlayUrl;
    @Column(name = "song_provider", length = 32)
    @Enumerated(EnumType.STRING)
    private SongProvider provider;
}
