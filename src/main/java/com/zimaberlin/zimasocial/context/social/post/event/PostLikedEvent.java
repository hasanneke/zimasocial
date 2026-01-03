package com.zimaberlin.zimasocial.context.social.post.event;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;

public record PostLikedEvent(Long postId, AuthorId postOwnerId, AuthorId actorId) {
}
