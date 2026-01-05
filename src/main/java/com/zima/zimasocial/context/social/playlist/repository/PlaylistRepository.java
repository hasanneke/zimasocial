package com.zima.zimasocial.context.social.playlist.repository;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.entity.Playlist;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository {
    List<Playlist> findByAuthorIdOrderByCreatedAtDesc(AuthorId authorId);
    long countByAuthorId(AuthorId authorId);
    void save(Playlist playlist);
    Optional<Playlist> findById(PlayListId id);
    void delete(Playlist playlist);
}
