package com.zima.zimasocial.service.notification;

import com.zima.zimasocial.entity.CommentEntity;
import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.PostEntity;
import com.zima.zimasocial.views.notification.NotificationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationView> getNotifications(Long userId, Pageable page);
    void sendPostLikedNotification(LikeEntity like);
    void sendPostCommentedNotification(CommentEntity comment);
    void sendCommentLikedNotification(CommentEntity comment);
    void sendCommentRepliedNotification(CommentEntity reply);
    void removePostLikedNotification(PostEntity post);
    void removePostCommentedNotification(CommentEntity comment);
    void removeCommentLikedNotification(CommentEntity comment);
    void removeCommentRepliedNotification(CommentEntity reply);
    void removeCommentReplyLikedNotification(CommentEntity reply);
}
