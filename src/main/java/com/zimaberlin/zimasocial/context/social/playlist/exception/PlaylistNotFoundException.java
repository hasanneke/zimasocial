package com.zimaberlin.zimasocial.context.social.playlist.exception;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class PlaylistNotFoundException extends DataNotFoundException {
    public PlaylistNotFoundException() {
        super("playlist_not_found", "Playlist not found");
    }
}
