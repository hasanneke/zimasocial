package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;

import java.util.Optional;

public interface MusicSearcher {
    Optional<MusicMedia> get(String id);
}
