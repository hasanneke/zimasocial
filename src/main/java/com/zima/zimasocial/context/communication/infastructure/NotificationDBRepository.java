package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.entity.*;
import com.zima.zimasocial.context.communication.domain.repository.NotificationRepository;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.communication.entity.Recipient;
import com.zima.zimasocial.context.communication.repository.RecipientRepository;
import com.zima.zimasocial.context.communication.entity.NotificationEntity;
import com.zima.zimasocial.context.communication.value.NotificationType;
import com.zima.zimasocial.context.communication.value.TargetCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationDBRepository implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;
    private final RecipientRepository recipientRepository;

    @Autowired
    public NotificationDBRepository(NotificationJpaRepository notificationJpaRepository, RecipientRepository recipientRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.recipientRepository = recipientRepository;
    }

    @Override
    public void save(Notification notification) {
        Recipient recipient = notification.getRecipientId() != null && notification.getRecipientId().getValue() != null ? recipientRepository.findById(notification.getRecipientId()).orElse(null) : null;
        Recipient actor = recipientRepository.findById(notification.getActorId()).orElse(null);
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
                case PostSharedNotification postSharedNotification -> notificationEntity = NotificationEntity.buildPostSharedNotification(postSharedNotification);
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

    @Override
    public Optional<Notification> getPreviousNotification(Notification notification) {
        switch (notification){
            case AuthorFollowRequestAcceptedNotification authorFollowRequestAcceptedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.USER_FOLLOW_REQUEST_ACCEPTED).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case AuthorFollowRequestSentNotification authorFollowRequestSentNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.USER_SENT_FOLLOW_REQUEST).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case AuthorFollowedNotification authorFollowedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.USER_FOLLOWED_YOU).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case ChatMessageSentNotification chatMessageSentNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.NEW_MESSAGE).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case CommentLikedNotification commentLikedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.COMMENT_LIKED, commentLikedNotification.getCommentId().getValue()).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case CommentRepliedNotification commentRepliedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.COMMENT_REPLIED, commentRepliedNotification.getReplyId().getValue()).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case PostCommentedNotification postCommentedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.POST_COMMENTED, postCommentedNotification.getPostId().getValue()).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case PostLikedNotification postLikedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.POST_LIKED, postLikedNotification.getPostId()).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case PostSharedNotification postSharedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndTypeOrderByCreatedAtDesc(postSharedNotification.getActorId().getValue(), NotificationType.POST_SHARED).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case SimpleNotification simpleNotification -> {
                return Optional.empty();
            }
        }
    }
}
