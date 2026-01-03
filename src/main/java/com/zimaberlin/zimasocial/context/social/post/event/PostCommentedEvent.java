package com.zimaberlin.zimasocial.context.social.post.event;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;

public record PostCommentedEvent(Long postId, Long commentId, AuthorId actorId, AuthorId commentOwnerId) {
}
