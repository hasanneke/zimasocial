package com.zimaberlin.zimasocial.context.social.post;

public record PostCommentedEvent(Long postId, Long commentId, Long actorId, Long commentOwnerId) {
}
