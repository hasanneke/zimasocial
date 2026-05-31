package com.zima.zimasocial.context.social2.domain.event;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;

public record AuthorFollowRequestAcceptedEvent (AuthorId followerAuthorId, AuthorId followedAuthorId){
}
