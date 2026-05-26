package com.zima.zimasocial.context.social2.event;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.CommentId;
import com.zima.zimasocial.context.social2.domain.value.PostId;

public record PostCommentedEvent(PostId postId, CommentId commentId, AuthorId actorId, AuthorId commentOwnerId) {
}
