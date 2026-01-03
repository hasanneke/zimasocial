package com.zimaberlin.zimasocial.context.social.playlist.infastructure;

import com.zimaberlin.zimasocial.context.social.playlist.values.PlayListItem;
import com.zimaberlin.zimasocial.context.social.post.value.MediaId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@Getter
public class PlaylistItemJpaEntity {
    @Column(name = "media_id", nullable = false)
    private UUID mediaItemId;

    public PlaylistItemJpaEntity(PlayListItem playListItem) {
        this.mediaItemId = playListItem.getMediaId().value();
    }

    public PlayListItem rehydrate() {
        return new PlayListItem(new MediaId(mediaItemId));
    }
}
