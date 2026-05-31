package com.zima.zimasocial.context.social2.playlist.infastructure;

import com.zima.zimasocial.context.social2.domain.value.MediaId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class PlaylistItem {
    @Embedded
    private MediaId mediaItemId;

    public PlaylistItem(MediaId mediaItemId) {
        this.mediaItemId = mediaItemId;
    }

}
