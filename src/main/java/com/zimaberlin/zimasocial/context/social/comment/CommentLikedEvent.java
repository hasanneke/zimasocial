package com.zimaberlin.zimasocial.context.social.comment;

public record CommentLikedEvent(Long commentId, Long commentOwnerId, Long likerAuthorId) {
}
