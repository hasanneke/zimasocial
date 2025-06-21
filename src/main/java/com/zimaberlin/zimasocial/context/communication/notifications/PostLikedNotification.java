package com.zimaberlin.zimasocial.context.communication.notifications;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class PostLikedNotification extends Notification {
    private Long postId;
}
