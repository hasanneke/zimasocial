package com.zimaberlin.zimasocial.context.communication.notifications;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public sealed class Notification permits AuthorFollowedNotification, CommentLikedNotification, CommentRepliedNotification, PostCommentedNotification, PostLikedNotification {
    Long recipientId;
    Long actorId;
    String message;
    LocalDateTime createdAt;
}
