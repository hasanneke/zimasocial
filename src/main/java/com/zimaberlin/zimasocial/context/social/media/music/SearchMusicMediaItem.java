package com.zimaberlin.zimasocial.context.social.media.music;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMusicMediaItem {
    private String id;
    private String name;
    private String uri;
    private String href;
    private String previewUrl;
    private MusicMedia.Artist artist;
    private MusicMedia.Album album;
    private String type;
    private String provider;

    @Getter
    @Setter
    public static class Artist {
        private String href;
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
