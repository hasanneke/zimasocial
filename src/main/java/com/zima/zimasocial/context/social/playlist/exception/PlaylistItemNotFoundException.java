package com.zima.zimasocial.context.social.playlist.exception;

import com.zima.zimasocial.exception.DataNotFoundException;

public class PlaylistItemNotFoundException extends DataNotFoundException {
    public PlaylistItemNotFoundException() {
        super("playlist_item_not_found");
    }
}
