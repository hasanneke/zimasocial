package com.zima.zimasocial.context.social.media.repository;

import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.value.MediaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaItemJpaRepository extends JpaRepository<Media, MediaId> {
    @Query("SELECT mediaItem.id FROM Media mediaItem WHERE mediaItem.resourceId = :resourceId AND mediaItem.provider = :provider")
    Optional<UUID> findIdByResourceIdAndProvider(String resourceId, String provider);
    Media findDistinctById(MediaId id);
}
