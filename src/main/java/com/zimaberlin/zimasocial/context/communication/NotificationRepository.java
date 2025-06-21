package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.notifications.CommentLikedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.CommentRepliedNotification;
import com.zimaberlin.zimasocial.context.communication.notifications.Notification;
import com.zimaberlin.zimasocial.context.communication.notifications.PostLikedNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepository {
    void save(Notification notification);
    Optional<PostLikedNotification> getPostLikedNotification(Long actorId, Long postId);
    Optional<CommentLikedNotification> getCommentLikedNotification(Long actorId, Long commentId);
}
