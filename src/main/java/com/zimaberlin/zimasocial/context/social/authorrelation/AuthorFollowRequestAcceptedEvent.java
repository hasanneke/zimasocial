package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record AuthorFollowRequestAcceptedEvent (AuthorId followerAuthorId, AuthorId followedAuthorId){
}
