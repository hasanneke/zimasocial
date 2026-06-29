package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.entity.*;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.context.communication.entity.NotificationEntity;

public class NotificationDBRepositoryAdapter {
    public static PostLikedNotification convertNotificationEntityToPostLikedNotification(NotificationEntity notificationEntity) {
        return PostLikedNotification.builder()
                .postId(notificationEntity.getPostId())
                .actorId(new RecipientId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .build();
    }

    public static PostCommentedNotification convertNotificationEntityToPostCommentedNotification(NotificationEntity notificationEntity) {
        return PostCommentedNotification.builder()
                .postId(new PostId(notificationEntity.getPostId()))
                .commentId(new CommentId(notificationEntity.getTargetId()))
                .actorId(new RecipientId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .build();
    }
    public static CommentLikedNotification convertNotificationEntityToCommentLikedNotification(NotificationEntity notificationEntity) {
        return CommentLikedNotification.builder()
                .postId(new PostId(notificationEntity.getPostId()))
                .commentId(new CommentId(notificationEntity.getTargetId()))
                .actorId(new RecipientId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .build();
    }
    public static CommentRepliedNotification convertCommentRepliedNotification(NotificationEntity notificationEntity) {
        return CommentRepliedNotification.builder()
                .postId(new PostId(notificationEntity.getPostId()))
                .commentId(new CommentId(notificationEntity.getTargetId()))
                .replyId(new CommentId(notificationEntity.getTargetId()))
                .actorId(new RecipientId(notificationEntity.getActorId()))
                .createdAt(notificationEntity.getCreatedAt())
                .message(notificationEntity.getContent())
                .build();
    }

    public static Notification convertToNotification(NotificationEntity notificationEntity) {
        switch (notificationEntity.getType()){
            case POST_SHARED -> {
                return PostSharedNotification
                        .builder()
                        .id(notificationEntity.getId())
                        .postId(notificationEntity.getPostId())
                        .content(notificationEntity.getContent())
                        .message(notificationEntity.getContent())
                        .type(notificationEntity.getPostType())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case POST_LIKED -> {
                return PostLikedNotification.builder()
                        .id(notificationEntity.getId())
                        .postId(notificationEntity.getPostId())
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case COMMENT_LIKED -> {
                return CommentLikedNotification.builder()
                        .id(notificationEntity.getId())
                        .postId(new PostId(notificationEntity.getPostId()))
                        .commentId(new CommentId(notificationEntity.getTargetId()))
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case USER_FOLLOWED_YOU -> {
                return AuthorFollowedNotification.builder()
                        .id(notificationEntity.getId())
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case USER_FOLLOW_REQUEST_ACCEPTED -> {
                return AuthorFollowRequestAcceptedNotification.builder()
                        .id(notificationEntity.getId())
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case USER_SENT_FOLLOW_REQUEST -> {
                return AuthorFollowRequestSentNotification.builder()
                        .id(notificationEntity.getId())
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case POST_COMMENTED -> {
                return PostCommentedNotification.builder()
                        .id(notificationEntity.getId())
                        .postId(new PostId(notificationEntity.getPostId()))
                        .commentId(new CommentId(notificationEntity.getTargetId()))
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case COMMENT_REPLIED -> {
                return CommentRepliedNotification.builder()
                        .id(notificationEntity.getId())
                        .postId(new PostId(notificationEntity.getPostId()))
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .replyId(new CommentId(notificationEntity.getTargetId()))
                        .createdAt(notificationEntity.getCreatedAt())
                        .build();
            }
            case NEW_MESSAGE -> {
                return ChatMessageSentNotification.builder()
                        .id(notificationEntity.getId())
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .chatRoomId(new ChatRoomId(notificationEntity.getChatId()))
                        .build();
            }
            case AUTHOR_ADDED_YOUR_MEDIA_TO_THEIR_LIST_NOTIFICATION ->  {
                return AuthorAddedYourMediaToTheirListNotification.builder()
                        .id(notificationEntity.getId())
                        .message(notificationEntity.getContent())
                        .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                        .actorId(new RecipientId(notificationEntity.getActorId()))
                        .isPushed(notificationEntity.getIsPushed())
                        .createdAt(notificationEntity.getCreatedAt())
                        .postIdReferencedFrom(new PostId(notificationEntity.getPostId()))
                        .build();
            }
            case POST_DELETED, VERY_IMPORTANT, DANGER, WELCOME, NEWS, IMPORTANT -> {
            }
        }
        return SimpleNotification.builder()
                .id(notificationEntity.getId())
                .actorId(new RecipientId(notificationEntity.getActorId()))
                .recipientId(new RecipientId(notificationEntity.getReceiverUserId()))
                .message(notificationEntity.getContent())
                .isPushed(notificationEntity.getIsPushed())
                .build();
    }
}
