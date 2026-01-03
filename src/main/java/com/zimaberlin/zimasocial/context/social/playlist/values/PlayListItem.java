package com.zimaberlin.zimasocial.context.social.playlist.values;

import com.zimaberlin.zimasocial.context.social.post.value.MediaId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PlayListItem {
    private final MediaId mediaId;
    public PlayListItem(MediaId mediaId) {
        this.mediaId = mediaId;
    }
    public MediaId getMediaId() {
        return mediaId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlayListItem that = (PlayListItem) o;
        return Objects.equals(mediaId, that.mediaId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mediaId);
    }
}
