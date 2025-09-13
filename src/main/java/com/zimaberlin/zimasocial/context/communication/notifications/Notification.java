package com.zimaberlin.zimasocial.context.communication.notifications;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public sealed class Notification permits AuthorFollowedNotification, CommentLikedNotification, CommentRepliedNotification, PostCommentedNotification, PostLikedNotification {
    AuthorId recipientId;
    AuthorId actorId;
    String message;
    LocalDateTime createdAt;
}
