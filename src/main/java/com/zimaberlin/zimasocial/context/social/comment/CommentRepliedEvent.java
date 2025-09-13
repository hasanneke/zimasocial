package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record CommentRepliedEvent(Long parentCommentId, Long replyId, AuthorId parentCommentOwnerId, AuthorId replyerId) {
}
