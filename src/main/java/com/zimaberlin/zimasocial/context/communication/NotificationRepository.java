package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.context.communication.notifications.CommentLikedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.CommentRepliedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.Notification;
import com.zimaberlin.zimasocial.context.communication.notifications.PostLikedNotification;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    void save(Notification notification);
    Optional<PostLikedNotification> getPostLikedNotification(RecipientId recipientId, Long postId);
    Optional<CommentLikedNotification> getCommentLikedNotification(RecipientId recipientId, Long commentId);
    List<Notification> findAllByIsPushedFalse();
}
