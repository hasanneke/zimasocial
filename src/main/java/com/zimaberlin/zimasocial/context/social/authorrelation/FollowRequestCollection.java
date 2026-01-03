package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import com.zimaberlin.zimasocial.context.social.authorrelation.entity.FollowRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRequestCollection {
    void save(FollowRequest followRequest);
    void delete(FollowRequest followRequest);
    Optional<FollowRequest> findById(UUID id);
    List<FollowRequest> findAllByFollowedAuthorIdAndUpdatedAtIsNull(AuthorId id);
    Integer countByFollowedAuthorId(AuthorId id);
    Optional<FollowRequest> findFirstByFollowedAuthorIdOrderByCreatedAtDesc(AuthorId id);
    Optional<FollowRequest> findByFollowedIdAndFollowerId(AuthorId followerId, AuthorId followedId);
    Integer countByFollowedAuthorIdAndIsApprovedFalse(AuthorId id);
}
