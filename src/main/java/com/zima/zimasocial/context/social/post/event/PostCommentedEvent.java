package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;

public record PostCommentedEvent(Long postId, Long commentId, AuthorId actorId, AuthorId commentOwnerId) {
}
