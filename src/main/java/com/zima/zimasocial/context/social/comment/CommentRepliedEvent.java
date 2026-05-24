package com.zima.zimasocial.context.social.comment;

public record CommentRepliedEvent(Long parentCommentId, Long replyId, Long parentCommentOwnerId, Long replyerId, Long postId) {
}
