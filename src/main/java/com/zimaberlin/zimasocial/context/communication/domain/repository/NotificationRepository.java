package com.zimaberlin.zimasocial.context.communication.domain.repository;

import com.zimaberlin.zimasocial.context.communication.domain.value.RecipientId;
import com.zimaberlin.zimasocial.context.communication.domain.entity.CommentLikedNotification;
import com.zimaberlin.zimasocial.context.communication.domain.entity.Notification;
import com.zimaberlin.zimasocial.context.communication.domain.entity.PostLikedNotification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    void save(Notification notification);
    Optional<PostLikedNotification> getPostLikedNotification(RecipientId recipientId, Long postId);
    Optional<CommentLikedNotification> getCommentLikedNotification(RecipientId recipientId, Long commentId);
    List<Notification> findAllByIsPushedFalse();
    Optional<Notification> getPreviousNotification(Notification notification);
}
