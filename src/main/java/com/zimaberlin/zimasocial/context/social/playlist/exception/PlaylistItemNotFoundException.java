package com.zimaberlin.zimasocial.context.social.playlist.exception;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class PlaylistItemNotFoundException extends DataNotFoundException {
    public PlaylistItemNotFoundException() {
        super("playlist_item_not_found");
    }
}
