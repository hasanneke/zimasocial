package com.zima.zimasocial.context.social.author.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;

public record AuthorFollowRequestAcceptedEvent (AuthorId followerAuthorId, AuthorId followedAuthorId){
}
