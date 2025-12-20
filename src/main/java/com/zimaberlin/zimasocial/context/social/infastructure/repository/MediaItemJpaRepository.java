package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.infastructure.jpaentities.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaItemJpaRepository extends JpaRepository<MediaItem, UUID> {
    Optional<MediaItem> findByResourceIdAndProvider(String resourceId, String provider);
}
