package com.zimaberlin.zimasocial.context.communication.domain.entity;

import com.zimaberlin.zimasocial.context.communication.domain.value.RecipientId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@SuperBuilder
@Getter
@NotNull
public abstract sealed class Notification permits AuthorFollowRequestAcceptedNotification, AuthorFollowRequestSentNotification, AuthorFollowedNotification, CommentLikedNotification, CommentRepliedNotification, PostCommentedNotification, PostLikedNotification, SimpleNotification, ChatMessageSentNotification {
    Long id;
    RecipientId recipientId;
    RecipientId actorId;
    String message;
    OffsetDateTime createdAt;
    boolean isPushed;
    public void push(){
        this.isPushed = true;
    }
}
