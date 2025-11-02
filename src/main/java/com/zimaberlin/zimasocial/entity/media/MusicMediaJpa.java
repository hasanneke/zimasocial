package com.zimaberlin.zimasocial.entity.media;

import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicMediaJpa {
    @Column(name = "song_resource_id", length = 128)
    private String id;
    @Column(name = "song_name")
    private String title;
    @Column(name = "singer_name", length = 64)
    private String artistName;
    @Column(name = "song_duration")
    private Long duration;
    @Column(name = "album_cover_url", length = 512)
    private String albumCoverUrl;
    @Column(name = "spotify_short_play_url", length = 512)
    private String spotifyShortPlayUrl;
    @Column(name = "song_provider", length = 32)
    @Enumerated(EnumType.STRING)
    private SongProvider provider;

    public MusicMediaJpa(MusicMedia musicMedia) {
        this.id = musicMedia.getResourceId();
        this.title = musicMedia.getSongTitle();
        this.artistName = musicMedia.getArtist() != null ? musicMedia.getArtist().getName() : null;
        this.albumCoverUrl = musicMedia.getAlbum() != null ? musicMedia.getAlbum().getImageUrl() : null;
        this.spotifyShortPlayUrl = musicMedia.getUri();
        this.provider = SongProvider.spotify;
        this.duration = musicMedia.getDuration();
    }
    public MusicMedia toMusicMedia(UUID mediaId) {
        MusicMedia.Album album = new MusicMedia.Album();
        MusicMedia.Artist artist = new MusicMedia.Artist();
        artist.setName(artistName);
        album.setImageUrl(albumCoverUrl);
        return MusicMedia.builder()
                .id(mediaId)
                .resourceId(id)
                .songTitle(title)
                .duration(duration)
                .album(album)
                .artist(artist)
                .type("track")
                .provider(provider)
                .build();
    }
}
