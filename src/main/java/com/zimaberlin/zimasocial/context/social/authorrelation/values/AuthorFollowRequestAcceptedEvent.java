package com.zimaberlin.zimasocial.context.social.authorrelation.values;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;

public record AuthorFollowRequestAcceptedEvent (AuthorId followerAuthorId, AuthorId followedAuthorId){
}
