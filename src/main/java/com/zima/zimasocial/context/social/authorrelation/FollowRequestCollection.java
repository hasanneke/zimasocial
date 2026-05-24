package com.zima.zimasocial.context.social.authorrelation;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRequestCollection {
    void save(FollowRequest followRequest);
    void delete(FollowRequest followRequest);
    Optional<FollowRequest> findById(UUID id);
    List<FollowRequest> findAllByFollowedAuthorIdAndUpdatedAtIsNull(AuthorDomainId id);
    Integer countByFollowedAuthorId(AuthorDomainId id);
    Optional<FollowRequest> findFirstByFollowedAuthorIdOrderByCreatedAtDesc(AuthorDomainId id);
    Optional<FollowRequest> findByFollowedIdAndFollowerId(AuthorDomainId followerId, AuthorDomainId followedId);
    Integer countByFollowedAuthorIdAndIsApprovedFalse(AuthorDomainId id);
}
