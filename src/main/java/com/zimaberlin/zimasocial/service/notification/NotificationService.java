package com.zimaberlin.zimasocial.service.notification;

import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationView> getNotifications(Long userId, Pageable pagea);
}
