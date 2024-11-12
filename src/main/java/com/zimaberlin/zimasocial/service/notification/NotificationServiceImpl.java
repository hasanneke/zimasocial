package com.zimaberlin.zimasocial.service.notification;

import com.zimaberlin.zimasocial.entity.NotificationEntity;
import com.zimaberlin.zimasocial.repository.NotificationRepository;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService{
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<NotificationView> getNotifications(Long userId, Pageable page) {
        Page<NotificationEntity> notificationEntities = notificationRepository.findByReceiverUserIdOrderByCreatedAt(userId, page);
        return notificationEntities.map((e)->NotificationView.builder()
                .url(e.getUrl())
                .type(e.getType())
                .content(e.getContent())
                .targetId(e.getTargetId())
                .targetCollection(e.getTargetCollection())
                .postId(e.getPostId())
                .createdAt(e.getCreatedAt())
                .build());
    }
}
