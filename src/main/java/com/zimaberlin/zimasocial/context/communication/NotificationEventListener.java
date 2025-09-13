package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.context.social.author.AuthorFollowedEvent;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.CommentLikedEvent;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepliedEvent;
import com.zimaberlin.zimasocial.context.social.post.PostCommentedEvent;
import com.zimaberlin.zimasocial.context.social.post.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                .actorId(postLikedEvent.actorId())
                .recipientId(postLikedEvent.postOwnerId())
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
                .actorId(postCommentedEvent.actorId())
                .recipientId(postCommentedEvent.commentOwnerId())
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
                .actorId(new AuthorId(commentLikedEvent.likerAuthorId().getId()))
                .recipientId(new AuthorId(commentLikedEvent.commentOwnerId().getId()))
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
                .actorId(commentRepliedEvent.replyerId())
                .recipientId(commentRepliedEvent.parentCommentOwnerId())
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendCommentRepliedNotification(commentRepliedNotification);
    }

    @EventListener
    public void handleAuthorFollowedEvent(AuthorFollowedEvent authorFollowedEvent) {
        AuthorFollowedNotification authorFollowedNotification = AuthorFollowedNotification
                .builder()
                .actorId(authorFollowedEvent.follower().getId())
                .recipientId(authorFollowedEvent.author().getId())
                .createdAt(LocalDateTime.now())
                .build();
        notificationService.sendAuthorFollowedYouNotification(authorFollowedNotification);
    }
}
