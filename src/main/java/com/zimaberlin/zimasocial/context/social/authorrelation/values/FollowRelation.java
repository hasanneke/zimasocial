package com.zimaberlin.zimasocial.context.social.authorrelation.values;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
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
