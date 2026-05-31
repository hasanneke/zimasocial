package com.zima.zimasocial.context.social2.playlist.exception;

import com.zima.zimasocial.exception.DataNotFoundException;

public class PlaylistNotFoundException extends DataNotFoundException {
    public PlaylistNotFoundException() {
        super("playlist_not_found", "Playlist not found");
    }
}
