package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.notifications.CommentLikedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.CommentRepliedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.PostCommentedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.PostLikedNotification;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.entity.NotificationEntity;

public class NotificationDBRepositoryAdapter {
    public static PostLikedNotification convertNotificationEntityToPostLikedNotification(NotificationEntity notificationEntity) {
        return PostLikedNotification.builder()
                .postId(notificationEntity.getPostId())
                .actorId(new AuthorId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .recipientId(new AuthorId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .build();
    }

    public static PostCommentedNotification convertNotificationEntityToPostCommentedNotification(NotificationEntity notificationEntity) {
        return PostCommentedNotification.builder()
                .postId(notificationEntity.getPostId())
                .commentId(notificationEntity.getTargetId())
                .actorId(new AuthorId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .recipientId(new AuthorId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .build();
    }
    public static CommentLikedNotification convertNotificationEntityToCommentLikedNotification(NotificationEntity notificationEntity) {
        return CommentLikedNotification.builder()
                .postId(notificationEntity.getPostId())
                .commentId(notificationEntity.getTargetId())
                .actorId(new AuthorId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .recipientId(new AuthorId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .build();
    }
    public static CommentRepliedNotification convertCommentRepliedNotification(NotificationEntity notificationEntity) {
        return CommentRepliedNotification.builder()
                .postId(notificationEntity.getPostId())
                .commentId(notificationEntity.getTargetId())
                .replyId(notificationEntity.getTargetId())
                .actorId(new AuthorId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .message(notificationEntity.getContent())
                .build();
    }
}
