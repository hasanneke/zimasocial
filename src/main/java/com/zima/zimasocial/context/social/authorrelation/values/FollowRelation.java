package com.zima.zimasocial.context.social.authorrelation.values;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import lombok.Getter;

@Getter
public final class FollowRelation extends AuthorRelation {
    private final AuthorDomainId followerId;
    private final AuthorDomainId followedId;

    public FollowRelation(AuthorDomainId followerId, AuthorDomainId followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }
}
