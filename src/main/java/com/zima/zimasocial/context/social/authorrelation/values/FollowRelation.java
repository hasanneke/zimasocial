package com.zima.zimasocial.context.social.authorrelation.values;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import lombok.Getter;

@Getter
public final class FollowRelation extends AuthorRelation {
    private final AuthorId followerId;
    private final AuthorId followedId;

    public FollowRelation(AuthorId followerId, AuthorId followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }
}
