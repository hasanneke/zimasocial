package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.PostId;

public record CommentRepliedEvent(CommentId parentCommentId, CommentId replyId, AuthorId parentCommentOwnerId, AuthorId replyerId, PostId postId) {
}
