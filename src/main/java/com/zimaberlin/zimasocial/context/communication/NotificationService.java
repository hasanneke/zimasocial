package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.notifications.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendPostLikedNotification(PostLikedNotification postLikedNotification) {
        Optional<PostLikedNotification> checkNotifiedBefore = notificationRepository.getPostLikedNotification(postLikedNotification.getActorId(), postLikedNotification.getPostId());
        if(checkNotifiedBefore.isEmpty()){
            notificationRepository.save(postLikedNotification);
        }
    }
    public void sendPostCommentedNotification(PostCommentedNotification postCommentedNotification) {
        notificationRepository.save(postCommentedNotification);
    }
    public void sendCommentLikedNotification(CommentLikedNotification commentLikedNotification) {
        Optional<CommentLikedNotification> checkNotifiedBefore = notificationRepository.getCommentLikedNotification(commentLikedNotification.getActorId(), commentLikedNotification.getCommentId());
        if(checkNotifiedBefore.isEmpty()){
            notificationRepository.save(commentLikedNotification);
        }
    }
    public void sendCommentRepliedNotification(CommentRepliedNotification commentRepliedNotification) {
        notificationRepository.save(commentRepliedNotification);
    }

    public void sendAuthorFollowedYouNotification(AuthorFollowedNotification authorFollowedNotification) {
        notificationRepository.save(authorFollowedNotification);
    }
}
