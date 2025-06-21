package com.zimaberlin.zimasocial.context.communication.notifications;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class PostCommentedNotification extends Notification {
    private Long postId;
    private Long commentId;
}
