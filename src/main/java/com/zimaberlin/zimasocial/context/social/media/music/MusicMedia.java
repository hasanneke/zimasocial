package com.zimaberlin.zimasocial.context.social.media.music;

import com.zimaberlin.zimasocial.entity.media.SongProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
public class MusicMedia {
    private UUID id;
    private String resourceId;
    private String songTitle;
    private String uri;
    private String previewUrl;
    private Artist artist;
    private Album album;
    private String type;
    private SongProvider provider;
    private Long duration;

    @Getter
    @Setter
    public static class Artist {
        private String id;
        private String name;
        private String uri;
    }

    @Getter
    @Setter
    public static class Album {
        private String albumType;
        private String name;
        private String imageUrl;
        private String uri;
        private String url;
    }
}
