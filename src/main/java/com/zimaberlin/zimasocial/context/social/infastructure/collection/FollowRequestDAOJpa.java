package com.zimaberlin.zimasocial.context.social.infastructure.collection;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRequest;
import com.zimaberlin.zimasocial.entity.FollowRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRequestDAOJpa extends JpaRepository<FollowRequestEntity, UUID> {
    List<FollowRequestEntity> findByFollowedIdAndUpdatedAtIsNull(Long id);
    Optional<FollowRequestEntity> findFirstByFollowedIdOrderByCreatedAtDesc(Long id);
    Optional<FollowRequestEntity> findFirstByFollowedIdAndFollowerId(Long followerId, Long followedId);
    Integer countByFollowedIdAndIsAcceptedFalse(Long id);
}
