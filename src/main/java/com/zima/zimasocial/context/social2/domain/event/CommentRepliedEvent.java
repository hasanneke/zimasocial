package com.zima.zimasocial.context.social2.domain.event;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.CommentId;
import com.zima.zimasocial.context.social2.domain.value.PostId;

public record CommentRepliedEvent(CommentId parentCommentId, CommentId replyId, AuthorId parentCommentOwnerId, AuthorId replyerId, PostId postId) {
}
