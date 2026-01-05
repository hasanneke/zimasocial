package com.zima.zimasocial.context.social.playlist.repository;

import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.context.social.playlist.values.PlayListItem;
import com.zima.zimasocial.context.social.playlist.values.PlayListItemId;
import com.zima.zimasocial.context.social.post.value.MediaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PlaylistItemRepository {
    Page<PlayListItem> findByPlaylistId(PlayListId playListId, Pageable pageable);
    List<PlayListItem> findAllByPlaylistId(PlayListId playListId);
    void save(PlayListItem item);
    void delete(PlayListItem playListItem);
    Optional<PlayListItem> findById(PlayListItemId playListItemId);
    Optional<PlayListItem> findByPlaylistIdAndMediaId(PlayListId playListId, MediaId mediaId);
}
