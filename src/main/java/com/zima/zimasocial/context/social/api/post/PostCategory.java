package com.zima.zimasocial.context.social.api.post;

import com.zima.zimasocial.entity.MediaType;

import java.util.Optional;

public enum PostCategory {
    all,
    music,
    movie,
    tv,
    book,
    followings;

    public Optional<MediaType> getType() {
        switch (this){
            case all:
                return Optional.empty();
            case music:
                return Optional.of(MediaType.music);
            case movie:
                return Optional.of(MediaType.movie);
            case tv:
                return Optional.of(MediaType.tv);
            case book:
                return Optional.of(MediaType.book);
            case followings:
                return Optional.empty();
            case null:
                return Optional.empty();
        }
    }
}
