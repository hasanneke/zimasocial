package com.zimaberlin.zimasocial.context.social.authorrelation;

import lombok.Getter;

@Getter
public final class FollowRelation extends AuthorRelation {
    private final Long followerId;
    private final Long followedId;

    public FollowRelation(Long followerId, Long followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }
}
