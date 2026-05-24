package com.zima.zimasocial.context.social.playlist.repository;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.context.social.playlist.entity.Playlist;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository {
    List<Playlist> findByAuthorIdOrderByCreatedAtDesc(AuthorDomainId authorId);
    List<PlaylistDTO> findAllWithCount(AuthorDomainId authorId);
    long countByAuthorId(AuthorDomainId authorId);
    void save(Playlist playlist);
    Optional<Playlist> findById(PlayListId id);
    void delete(Playlist playlist);
}
