package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;

public record PostLikedEvent(Long postId, AuthorId postOwnerId, AuthorId actorId) {
}
