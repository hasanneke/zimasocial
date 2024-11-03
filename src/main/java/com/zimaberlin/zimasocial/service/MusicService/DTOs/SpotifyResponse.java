package com.zimaberlin.zimasocial.service.MusicService.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyResponse {
    private SpotifyTracks tracks;

    @Getter
    public static class SpotifyTracks {
        private List<SpotifyMusic> items = new ArrayList<>();
        private int limit;
        private int offset;
        private int total;
        private String next;
        private String previous;
        private String href;
    }

    @Getter
    public static class SpotifyMusic {
        private String id;
        private String name;
        private String preview_url; // Changed from previewUrl to match JSON
        private String type;
        private String uri;
        private ExternalUrls external_urls; // Changed from externalUrls to match JSON
        private String href;
        private List<Artist> artists; // Added missing artists array
        private Album album; // Added missing album object
        private int duration_ms; // Added missing duration
        private boolean explicit;
        private int popularity;
    }

    @Getter
    public static class Artist {
        private String id;
        private String href;
        private String name;
        private String type;
        private String uri;
        private ExternalUrls external_urls; // Changed from externalUrl to match JSON
    }

    @Getter
    public static class ExternalUrls { // Changed from ExternalUrl to match JSON
        private String spotify;
    }

    @Getter
    public static class Album {
        private String id;
        private String name;
        private String album_type;
        private List<Image> images;
        private String release_date;
        private String type;
        private ExternalUrls external_urls;
    }

    @Getter
    public static class Image {
        private int height;
        private int width;
        private String url;
    }
}