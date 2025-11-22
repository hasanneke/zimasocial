package com.zimaberlin.zimasocial.context.communication.domain.entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class CommentLikedNotification extends Notification {
    private Long postId;
    private Long commentId;
}
