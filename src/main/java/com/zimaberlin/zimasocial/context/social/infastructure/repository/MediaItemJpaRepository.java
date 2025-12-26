package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaItemJpaRepository extends JpaRepository<MediaItem, UUID> {
    Optional<MediaItem> findByResourceIdAndProvider(String resourceId, String provider);
    @Query("SELECT mediaItem.id FROM MediaItem mediaItem WHERE mediaItem.resourceId = :resourceId AND mediaItem.provider = :provider")
    Optional<UUID> findIdByResourceIdAndProvider(String resourceId, String provider);
}
