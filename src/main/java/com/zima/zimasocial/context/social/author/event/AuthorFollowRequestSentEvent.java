package com.zima.zimasocial.context.social.author.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;

public record AuthorFollowRequestSentEvent(AuthorId followerId, AuthorId followedId) {
}
