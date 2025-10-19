package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.chat.event.ChatMessageSentEvent;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.context.social.author.AuthorFollowRequestSentEvent;
import com.zimaberlin.zimasocial.context.social.author.AuthorFollowedEvent;
import com.zimaberlin.zimasocial.context.social.authorrelation.AuthorFollowRequestAcceptedEvent;
import com.zimaberlin.zimasocial.context.social.comment.CommentLikedEvent;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepliedEvent;
import com.zimaberlin.zimasocial.context.social.post.PostCommentedEvent;
import com.zimaberlin.zimasocial.context.social.post.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;
    @EventListener
    public void handlePostLikedEvent(PostLikedEvent postLikedEvent) {
        if(postLikedEvent.postOwnerId().equals(postLikedEvent.actorId())){
            return;
        }
        PostLikedNotification postLikedNotification = PostLikedNotification.builder()
                .postId(postLikedEvent.postId())
                .actorId(new RecipientId(postLikedEvent.actorId().getId()))
                .recipientId(new RecipientId(postLikedEvent.postOwnerId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendPostLikedNotification(postLikedNotification);
    }

    @EventListener
    public void handlePostCommentedEvent(PostCommentedEvent postCommentedEvent) {
        if(postCommentedEvent.commentOwnerId().equals(postCommentedEvent.actorId())){
            return;
        }
        PostCommentedNotification postCommentedNotification = PostCommentedNotification.builder()
                .postId(postCommentedEvent.postId())
                .actorId(new RecipientId(postCommentedEvent.actorId().getId()))
                .recipientId(new RecipientId(postCommentedEvent.commentOwnerId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendPostCommentedNotification(postCommentedNotification);
    }


    @EventListener
    public void handleCommentLikedEvent(CommentLikedEvent commentLikedEvent) {
        if(commentLikedEvent.commentOwnerId().equals(commentLikedEvent.likerAuthorId())){
            return;
        }
        CommentLikedNotification commentLikedNotification = CommentLikedNotification.builder()
                .postId(commentLikedEvent.postId())
                .commentId(commentLikedEvent.commentId())
                .actorId(new RecipientId(commentLikedEvent.likerAuthorId().getId()))
                .recipientId(new RecipientId(commentLikedEvent.commentOwnerId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendCommentLikedNotification(commentLikedNotification);
    }

    @EventListener
    public void   handleCommentRepliedEvent(CommentRepliedEvent commentRepliedEvent) {
        if(commentRepliedEvent.parentCommentOwnerId().equals(commentRepliedEvent.replyerId())){
            return;
        }
        CommentRepliedNotification commentRepliedNotification = CommentRepliedNotification.builder()
                .commentId(commentRepliedEvent.parentCommentId())
                .actorId(new RecipientId(commentRepliedEvent.replyerId().getId()))
                .recipientId(new RecipientId(commentRepliedEvent.parentCommentOwnerId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendCommentRepliedNotification(commentRepliedNotification);
    }

    @EventListener
    public void handleAuthorFollowedEvent(AuthorFollowedEvent authorFollowedEvent) {
        AuthorFollowedNotification authorFollowedNotification = AuthorFollowedNotification
                .builder()
                .actorId(new RecipientId(authorFollowedEvent.follower().getId().getId()))
                .recipientId(new RecipientId(authorFollowedEvent.followed().getId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendAuthorFollowedYouNotification(authorFollowedNotification);
    }

    @EventListener
    public void handleAuthorFollowedRequestAcceptedEvent(AuthorFollowRequestAcceptedEvent authorFollowRequestAcceptedEvent) {
        AuthorFollowRequestAcceptedNotification authorFollowedNotification = AuthorFollowRequestAcceptedNotification
                .builder()
                .actorId(new RecipientId(authorFollowRequestAcceptedEvent.followedAuthorId().getId()))
                .recipientId(new RecipientId(authorFollowRequestAcceptedEvent.followerAuthorId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendAuthorFollowedRequestAcceptedNotification(authorFollowedNotification);
    }

    @EventListener
    public void handleAuthorFollowRequestSentEvent(AuthorFollowRequestSentEvent authorFollowRequestSentEvent) {
        AuthorFollowRequestSentNotification authorFollowedNotification = AuthorFollowRequestSentNotification
                .builder()
                .actorId(new RecipientId(authorFollowRequestSentEvent.followerId().getId()))
                .recipientId(new RecipientId(authorFollowRequestSentEvent.followedId().getId()))
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendAuthorSentFollowRequestNotification(authorFollowedNotification);
    }

    @EventListener
    public void handleChatMessageSentEvent(ChatMessageSentEvent chatMessageSentEvent) {

    }
}
