package com.zima.zimasocial.context.communication.domain.entity;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@SuperBuilder
@Getter
@NotNull
public abstract sealed class Notification permits AuthorAddedYourMediaToTheirListNotification, AuthorFollowRequestAcceptedNotification, AuthorFollowRequestSentNotification, AuthorFollowedNotification, ChatMessageSentNotification, CommentLikedNotification, CommentRepliedNotification, PostCommentedNotification, PostLikedNotification, PostSharedNotification, SimpleNotification {
    Long id;
    @Setter
    RecipientId recipientId;
    RecipientId actorId;
    String message;
    OffsetDateTime createdAt;
    boolean isPushed;
    public void push(){
        this.isPushed = true;
    }
}
