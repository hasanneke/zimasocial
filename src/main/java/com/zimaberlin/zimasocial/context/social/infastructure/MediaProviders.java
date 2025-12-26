package com.zimaberlin.zimasocial.context.social.infastructure;

import com.zimaberlin.zimasocial.entity.PostType;
import lombok.Getter;

@Getter
public enum MediaProviders {
    SPOTIFY("spotify"),
    TDMB("tdmb"),
    GOOGLE_BOOKS("google_books");
    private String name;
    MediaProviders(String name){
        this.name = name;
    }

    public static MediaProviders findByPostType(PostType type){
        switch (type){
            case PostType.music -> {
                return SPOTIFY;
            }
            case PostType.movie -> {
                return TDMB;
            }
            case PostType.book -> {
                return GOOGLE_BOOKS;
            }
            default -> {
                return null;
            }
        }
    }
}
