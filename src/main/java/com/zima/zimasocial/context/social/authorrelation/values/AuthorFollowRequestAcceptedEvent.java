package com.zima.zimasocial.context.social.authorrelation.values;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

public record AuthorFollowRequestAcceptedEvent (AuthorDomainId followerAuthorId, AuthorDomainId followedAuthorId){
}
