package com.zimaberlin.zimasocial.context.social.post.event;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record PostLikedEvent(Long postId, AuthorId postOwnerId, AuthorId actorId) {
}
