package com.zima.zimasocial.context.social.authorrelation.values;

import com.zima.zimasocial.context.social.author.value.AuthorId;

public record AuthorFollowRequestAcceptedEvent (AuthorId followerAuthorId, AuthorId followedAuthorId){
}
