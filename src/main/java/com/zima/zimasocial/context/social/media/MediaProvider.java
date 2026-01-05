package com.zima.zimasocial.context.social.media;

import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;

@Getter
public enum MediaProvider {
    SPOTIFY("spotify"),
    TMDB_MOVIE("tmdbMovie"),
    TMDB_TV("tmdbTv"),
    GOOGLE_BOOKS("googleBooks");
    private String name;
    MediaProvider(String name){
        this.name = name;
    }

    public static MediaProvider findByPostType(MediaType type){
        switch (type){
            case MediaType.music -> {
                return SPOTIFY;
            }
            case MediaType.movie -> {
                return TMDB_MOVIE;
            }
            case MediaType.book -> {
                return GOOGLE_BOOKS;
            }
            case MediaType.tv -> {
                return TMDB_TV;
            }
            default -> {
                return null;
            }
        }
    }
}
