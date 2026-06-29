package com.zima.zimasocial.context.social.playlist.event;

import com.zima.zimasocial.context.social.playlist.infastructure.PlaylistItem;
import com.zima.zimasocial.context.social.post.value.PostId;

public record PlaylistItemAdded(PlaylistItem item, PostId postIdReferencedFrom) {
}
