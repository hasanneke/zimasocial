package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record PostLikedEvent(Long postId, AuthorId postOwnerId, AuthorId actorId) {
}
