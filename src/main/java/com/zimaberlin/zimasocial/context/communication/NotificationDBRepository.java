package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.entity.NotificationEntity;
import com.zimaberlin.zimasocial.entity.NotificationType;
import com.zimaberlin.zimasocial.entity.TargetCollection;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.NotificationJpaRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        UserEntity recipient = userJpaRepository.findById(notification.getRecipientId().getValue()).orElse(null);
        UserEntity actor = userJpaRepository.findById(notification.getActorId().getValue()).orElse(null);
        NotificationEntity notificationEntity = notificationJpaRepository.findById(notification.getId() == null ? -1 : notification.getId()).orElse(null);
        if(notificationEntity == null){
            switch (notification){
                case PostLikedNotification postLikedNotification -> notificationEntity =  NotificationEntity.buildPostLikedNotification(postLikedNotification, recipient, actor);
                case PostCommentedNotification postCommentedNotification -> notificationEntity =  NotificationEntity.buildPostCommentedNotification(postCommentedNotification, recipient, actor);
                case CommentLikedNotification commentLikedNotification -> notificationEntity =  NotificationEntity.buildCommentLikedNotification(commentLikedNotification, recipient, actor);
                case CommentRepliedNotification commentRepliedNotification -> notificationEntity =  NotificationEntity.buildCommentRepliedNotification(commentRepliedNotification, recipient, actor);
                case AuthorFollowedNotification authorFollowedNotification -> notificationEntity = NotificationEntity.buildAuthorFollowedNotification(authorFollowedNotification);
                case AuthorFollowRequestSentNotification authorFollowRequestSentNotification -> notificationEntity = NotificationEntity.buildAuthorFollowRequestSentNotification(authorFollowRequestSentNotification);
                case AuthorFollowRequestAcceptedNotification authorFollowRequestAcceptedNotification -> notificationEntity = NotificationEntity.buildAuthorFollowRequestAcceptedNotification(authorFollowRequestAcceptedNotification);
                case ChatMessageSentNotification chatMessageSentNotification -> notificationEntity = NotificationEntity.buildNewMessageNotification(chatMessageSentNotification);
                default -> throw new IllegalStateException("Unexpected value: " + notification);
            }
        }else{
            notificationEntity.merge(notification);
        }
        if(notificationEntity != null){
            notificationJpaRepository.save(notificationEntity);
        }
    }

    @Override
    public Optional<PostLikedNotification> getPostLikedNotification(RecipientId actorId, Long postId) {
        return notificationJpaRepository.findByActorIdAndTargetIdAndTypeAndTargetCollection(actorId.getValue(), postId, NotificationType.POST_LIKED, TargetCollection.post).map(NotificationDBRepositoryAdapter::convertNotificationEntityToPostLikedNotification);
    }

    @Override
    public Optional<CommentLikedNotification> getCommentLikedNotification(RecipientId actorId, Long commentId) {
        return notificationJpaRepository.findByActorIdAndTargetIdAndTypeAndTargetCollection(actorId.getValue(), commentId, NotificationType.COMMENT_LIKED, TargetCollection.comment).map(NotificationDBRepositoryAdapter::convertNotificationEntityToCommentLikedNotification);
    }

    @Override
    public List<Notification> findAllByIsPushedFalse() {
        return notificationJpaRepository.findAllByIsPushedFalse().stream().map(NotificationDBRepositoryAdapter::convertToNotification).toList();
    }
}
