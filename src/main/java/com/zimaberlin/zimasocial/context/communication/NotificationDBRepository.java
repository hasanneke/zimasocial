package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.entity.NotificationEntity;
import com.zimaberlin.zimasocial.entity.NotificationType;
import com.zimaberlin.zimasocial.entity.TargetCollection;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.NotificationJpaRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class NotificationDBRepository implements NotificationRepository{
    private final NotificationJpaRepository notificationJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public NotificationDBRepository(NotificationJpaRepository notificationJpaRepository, UserJpaRepository userJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void save(Notification notification) {
        UserEntity recipient = userJpaRepository.findById(notification.getRecipientId()).orElse(null);
        UserEntity actor = userJpaRepository.findById(notification.getActorId()).orElse(null);
        NotificationEntity notificationEntity;
        switch (notification){
            case PostLikedNotification postLikedNotification -> {
                notificationEntity =  NotificationEntity.buildPostLikedNotification(postLikedNotification, recipient, actor);
            }
            case PostCommentedNotification postCommentedNotification -> {
                notificationEntity =  NotificationEntity.buildPostCommentedNotification(postCommentedNotification, recipient, actor);
            }
            case CommentLikedNotification commentLikedNotification -> {
                notificationEntity =  NotificationEntity.buildCommentLikedNotification(commentLikedNotification, recipient, actor);
            }
            case CommentRepliedNotification commentRepliedNotification -> {
                notificationEntity =  NotificationEntity.buildCommentRepliedNotification(commentRepliedNotification, recipient, actor);
            }
            case AuthorFollowedNotification authorFollowedNotification -> {
                notificationEntity = NotificationEntity.buildAuthorFollowedNotification(authorFollowedNotification);
            }
            default -> throw new IllegalStateException("Unexpected value: " + notification);
        }
        notificationJpaRepository.save(notificationEntity);

    }

    @Override
    public Optional<PostLikedNotification> getPostLikedNotification(Long actorId, Long postId) {
        return notificationJpaRepository.findByActorIdAndTargetIdAndTypeAndTargetCollection(actorId, postId, NotificationType.POST_LIKED, TargetCollection.post).map(NotificationDBRepositoryAdapter::convertNotificationEntityToPostLikedNotification);
    }

    @Override
    public Optional<CommentLikedNotification> getCommentLikedNotification(Long actorId, Long commentId) {
        return notificationJpaRepository.findByActorIdAndTargetIdAndTypeAndTargetCollection(actorId, commentId, NotificationType.COMMENT_LIKED, TargetCollection.comment).map(NotificationDBRepositoryAdapter::convertNotificationEntityToCommentLikedNotification);
    }
}
