package com.zima.zimasocial.context.communication.domain.entity;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@SuperBuilder
@Getter
@NotNull
public abstract sealed class Notification permits AuthorFollowRequestAcceptedNotification, AuthorFollowRequestSentNotification, AuthorFollowedNotification, ChatMessageSentNotification, CommentLikedNotification, CommentRepliedNotification, PostCommentedNotification, PostLikedNotification, PostSharedNotification, SimpleNotification {
    Long id;
    RecipientId recipientId;
    RecipientId actorId;
    String message;
    OffsetDateTime createdAt;
    boolean isPushed;
    public void push(){
        this.isPushed = true;
    }
    public void setRecipientId(RecipientId recipientId) {
        this.recipientId = recipientId;
    }
}
