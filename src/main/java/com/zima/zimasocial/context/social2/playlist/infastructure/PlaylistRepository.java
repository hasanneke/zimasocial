package com.zima.zimasocial.context.social2.playlist.infastructure;

import com.zima.zimasocial.context.social2.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
    long countByOwnerId(AuthorId authorId);
    @Query(
            """
                SELECT new com.zima.zimasocial.context.social2.playlist.api.dto.PlaylistDTO(
                            playlist.id,
                            playlist.name,
                            playlist.type,
                            COUNT(i)
                ) FROM  Playlist playlist
                LEFT JOIN playlist.items i
                WHERE playlist.ownerId = :userId
                GROUP BY playlist.id, playlist.name
                ORDER BY playlist.createdAt DESC
            """
    )
    List<PlaylistDTO> findAllWithCount(AuthorId userId);
}
