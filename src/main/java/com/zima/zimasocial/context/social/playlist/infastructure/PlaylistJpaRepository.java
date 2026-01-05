package com.zima.zimasocial.context.social.playlist.infastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistJpaRepository extends JpaRepository<PlaylistJpaEntity, UUID> {
    List<PlaylistJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserId(Long userId);
}
