package com.zimaberlin.zimasocial.service.musicService.domain;

import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MusicResponseView {
    private int offset;
    private int limit;
    private List<MusicView> items = List.of();
    private String provider;
    private int total;
    @Getter
    @Setter
    public static class MusicView {
        private String id;
        private String name;
        private String uri;
        private String previewUrl; // Changed from previewUrl to match JSON
        private MusicMedia.Artist artist;
        private MusicMedia.Album album;
        private String type;
        private String provider;
    }
}
