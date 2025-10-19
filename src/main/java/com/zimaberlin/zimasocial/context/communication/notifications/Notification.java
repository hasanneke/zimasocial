package com.zimaberlin.zimasocial.context.communication.notifications;

import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@NotNull
public sealed class Notification permits AuthorFollowRequestAcceptedNotification, AuthorFollowRequestSentNotification, AuthorFollowedNotification, CommentLikedNotification, CommentRepliedNotification, PostCommentedNotification, PostLikedNotification, SimpleNotification, ChatMessageSentNotification {
    Long id;
    RecipientId recipientId;
    RecipientId actorId;
    String message;
    LocalDateTime createdAt;
    boolean isPushed;
    public void push(){
        this.isPushed = true;
    }
}
