package com.zima.zimasocial.context.social.infastructure.collection;
import com.zima.zimasocial.entity.FollowRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRequestDAOJpa extends JpaRepository<FollowRequestEntity, UUID> {
    List<FollowRequestEntity> findByFollowedIdAndUpdatedAtIsNull(Long id);
    Optional<FollowRequestEntity> findFirstByFollowedIdOrderByCreatedAtDesc(Long id);
    Optional<FollowRequestEntity> findByFollowedIdAndFollowerId(Long followerId, Long followedId);
    Integer countByFollowedIdAndIsAcceptedFalse(Long id);
}
