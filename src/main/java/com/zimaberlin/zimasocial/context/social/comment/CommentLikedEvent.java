package com.zimaberlin.zimasocial.context.social.comment;

public record CommentLikedEvent(Long postId, Long commentId, Long commentOwnerId, Long likerAuthorId) {
}
