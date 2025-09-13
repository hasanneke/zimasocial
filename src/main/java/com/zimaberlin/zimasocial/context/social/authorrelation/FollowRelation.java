package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
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
