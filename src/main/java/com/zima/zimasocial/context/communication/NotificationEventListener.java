package com.zima.zimasocial.context.communication;

import com.zima.zimasocial.context.communication.application.NotificationManager;
import com.zima.zimasocial.context.communication.domain.entity.*;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.author.event.AuthorFollowRequestSentEvent;
import com.zima.zimasocial.context.social.author.event.AuthorFollowedEvent;
import com.zima.zimasocial.context.social.author.event.AuthorFollowRequestAcceptedEvent;
import com.zima.zimasocial.context.social.chat.event.ChatMessageSentEvent;
import com.zima.zimasocial.context.social.post.event.CommentLikedEvent;
import com.zima.zimasocial.context.social.post.event.CommentRepliedEvent;
import com.zima.zimasocial.context.social.post.event.PostCommentedEvent;
import com.zima.zimasocial.context.social.post.event.PostLikedEvent;
import com.zima.zimasocial.context.social.post.event.PostSharedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;


@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationManager notificationManager;

    @EventListener
    public void handlePostSharedEvent(PostSharedEvent postSharedEvent) {
        PostSharedNotification postSharedNotification = PostSharedNotification
                .builder()
                .postId(postSharedEvent.postId())
                .actorId(new RecipientId(postSharedEvent.postOwnerId().getValue()))
                .type(postSharedEvent.postContent().getType())
                .createdAt(OffsetDateTime.now())
                .content(postSharedEvent.postContent().getText() != null ? postSharedEvent
                        .postContent()
                        .getText()
                        .substring(0, Math.min(postSharedEvent.postContent().getText().length(), 100)) : null)
                .build();
        notificationManager.throttled().sendNotification(postSharedNotification);
    }

    @EventListener
    public void handlePostLikedEvent(PostLikedEvent postLikedEvent) {
        if(postLikedEvent.postOwnerId().equals(postLikedEvent.actorId())){
            return;
        }
        PostLikedNotification postLikedNotification = PostLikedNotification.builder()
                .postId(postLikedEvent.postId().getValue())
                .actorId(new RecipientId(postLikedEvent.actorId().getValue()))
                .recipientId(new RecipientId(postLikedEvent.postOwnerId().getValue()))
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.throttled().sendNotification(postLikedNotification);
    }

    @EventListener
    public void handlePostCommentedEvent(PostCommentedEvent postCommentedEvent) {
        if(postCommentedEvent.commentOwnerId().equals(postCommentedEvent.actorId())){
            return;
        }
        PostCommentedNotification postCommentedNotification = PostCommentedNotification.builder()
                .postId(postCommentedEvent.postId())
                .actorId(new RecipientId(postCommentedEvent.actorId().getValue()))
                .recipientId(new RecipientId(postCommentedEvent.commentOwnerId().getValue()))
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.sendNotification(postCommentedNotification);
    }


    @EventListener
    public void handleCommentLikedEvent(CommentLikedEvent commentLikedEvent) {
        if(commentLikedEvent.commentOwnerId().equals(commentLikedEvent.likerAuthorId())){
            return;
        }
        CommentLikedNotification commentLikedNotification = CommentLikedNotification.builder()
                .postId(commentLikedEvent.postId())
                .commentId(commentLikedEvent.commentId())
                .actorId(new RecipientId(commentLikedEvent.likerAuthorId().getValue()))
                .recipientId(new RecipientId(commentLikedEvent.commentOwnerId().getValue()))
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.throttled().sendNotification(commentLikedNotification);
    }

    @EventListener
    public void handleCommentRepliedEvent(CommentRepliedEvent commentRepliedEvent) {
        if(commentRepliedEvent.parentCommentOwnerId().equals(commentRepliedEvent.replyerId())){
            return;
        }
        CommentRepliedNotification commentRepliedNotification = CommentRepliedNotification.builder()
                .commentId(commentRepliedEvent.parentCommentId())
                .actorId(new RecipientId(commentRepliedEvent.replyerId().getValue()))
                .recipientId(new RecipientId(commentRepliedEvent.parentCommentOwnerId().getValue()))
                .postId(commentRepliedEvent.postId())
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.sendNotification(commentRepliedNotification);
    }

    @EventListener
    public void handleAuthorFollowedEvent(AuthorFollowedEvent authorFollowedEvent) {
        AuthorFollowedNotification authorFollowedNotification = AuthorFollowedNotification
                .builder()
                .actorId(new RecipientId(authorFollowedEvent.follower().getId().getValue()))
                .recipientId(new RecipientId(authorFollowedEvent.followed().getId().getValue()))
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.throttled().sendNotification(authorFollowedNotification);
    }

    @EventListener
    public void handleAuthorFollowedRequestAcceptedEvent(AuthorFollowRequestAcceptedEvent authorFollowRequestAcceptedEvent) {
        AuthorFollowRequestAcceptedNotification authorFollowedNotification = AuthorFollowRequestAcceptedNotification
                .builder()
                .actorId(new RecipientId(authorFollowRequestAcceptedEvent.followedAuthorId().getValue()))
                .recipientId(new RecipientId(authorFollowRequestAcceptedEvent.followerAuthorId().getValue()))
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.sendNotification(authorFollowedNotification);
    }

    @EventListener
    public void handleAuthorFollowRequestSentEvent(AuthorFollowRequestSentEvent authorFollowRequestSentEvent) {
        AuthorFollowRequestSentNotification authorFollowedNotification = AuthorFollowRequestSentNotification
                .builder()
                .actorId(new RecipientId(authorFollowRequestSentEvent.followerId().getValue()))
                .recipientId(new RecipientId(authorFollowRequestSentEvent.followedId().getValue()))
                .createdAt(OffsetDateTime.now())
                .build();
        notificationManager.sendNotification(authorFollowedNotification);
    }

    @EventListener
    public void handleChatMessageSentEvent(ChatMessageSentEvent chatMessageSentEvent) {
        ChatMessageSentNotification chatMessageSentNotification = ChatMessageSentNotification
                .builder()
                .chatRoomId(chatMessageSentEvent.message().getChatRoomId())
                .actorId(new RecipientId(chatMessageSentEvent.sender().getId().getValue()))
                .recipientId(new RecipientId(chatMessageSentEvent.receiver().getId().getValue()))
                .createdAt(OffsetDateTime.now())
                .message(chatMessageSentEvent.message().getContent())
                .chatRoomId(chatMessageSentEvent.message().getChatRoomId())
                .build();
        notificationManager.throttled().sendNotification(chatMessageSentNotification);
    }
}
