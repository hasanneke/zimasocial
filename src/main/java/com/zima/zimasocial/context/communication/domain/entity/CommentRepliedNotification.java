package com.zima.zimasocial.context.communication.domain.entity;

import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.PostId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class CommentRepliedNotification extends Notification{
    private PostId postId;
    private CommentId commentId;
    private CommentId replyId;
}
