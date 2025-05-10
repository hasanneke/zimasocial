package com.zimaberlin.zimasocial.service.notification;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationView> getNotifications(Long userId, Pageable page);
    void sendPostLikedNotification(LikeEntity like);
    void sendPostCommentedNotification(CommentEntity comment);
    void sendCommentLikedNotification(CommentEntity comment);
    void sendCommentRepliedNotification(CommentEntity reply);
}
