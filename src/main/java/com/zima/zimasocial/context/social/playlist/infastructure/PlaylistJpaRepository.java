package com.zima.zimasocial.context.social.playlist.infastructure;

import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistJpaRepository extends JpaRepository<PlaylistJpaEntity, UUID> {
    List<PlaylistJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserId(Long userId);


    @Query(
            """
                SELECT new com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO(
                            playlist.id,
                            playlist.name,
                            playlist.type,
                            COUNT(i)
                ) FROM  PlaylistJpaEntity playlist
                LEFT JOIN playlist.items i
                WHERE playlist.userId = :userId
                GROUP BY playlist.id, playlist.name
                ORDER BY playlist.createdAt DESC
            """
    )
    List<PlaylistDTO> findAllWithCount(Long userId);
}
