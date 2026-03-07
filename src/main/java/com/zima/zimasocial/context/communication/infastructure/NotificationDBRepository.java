package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.repository.NotificationRepository;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.communication.domain.entity.*;
import com.zima.zimasocial.entity.NotificationEntity;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.entity.TargetCollection;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.NotificationJpaRepository;
import com.zima.zimasocial.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationDBRepository implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public NotificationDBRepository(NotificationJpaRepository notificationJpaRepository, UserJpaRepository userJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void save(Notification notification) {
        UserEntity recipient = notification.getRecipientId() != null ? userJpaRepository.findById(notification.getRecipientId().getValue()).orElse(null) : null;
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
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.COMMENT_LIKED, commentLikedNotification.getCommentId()).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case CommentRepliedNotification commentRepliedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.COMMENT_REPLIED, commentRepliedNotification.getReplyId()).map(NotificationDBRepositoryAdapter::convertToNotification);
            }
            case PostCommentedNotification postCommentedNotification -> {
                return notificationJpaRepository.findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(notification.getActorId().getValue(), notification.getRecipientId().getValue(), NotificationType.POST_COMMENTED, postCommentedNotification.getPostId()).map(NotificationDBRepositoryAdapter::convertToNotification);
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
