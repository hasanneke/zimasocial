package com.zimaberlin.zimasocial.service.musicService.domain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
@Setter
public class MusicResponseView {
    private String href;
    private String next;
    private String previous;
    private int offset;
    private int limit;
    private List<MusicView> items = List.of();
    private String provider;
    @Getter
    @Setter
    public static class MusicView {
        private String id;
        private String name;
        private String uri;
        private String href;
        private String previewUrl; // Changed from previewUrl to match JSON
        private Artist artist;
        private Album album;
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
}
