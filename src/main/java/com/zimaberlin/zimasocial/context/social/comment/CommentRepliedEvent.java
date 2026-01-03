package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;

public record CommentRepliedEvent(Long parentCommentId, Long replyId, AuthorId parentCommentOwnerId, AuthorId replyerId) {
}
