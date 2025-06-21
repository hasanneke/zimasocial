package com.zimaberlin.zimasocial.context.social.comment;

public record CommentRepliedEvent(Long parentCommentId, Long replyId, Long parentCommentOwnerId, Long replyerId) {
}
