package com.zimaberlin.zimasocial.context.social.post.event;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record PostCommentedEvent(Long postId, Long commentId, AuthorId actorId, AuthorId commentOwnerId) {
}
