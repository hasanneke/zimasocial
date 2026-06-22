package com.zima.zimasocial.context.communication.domain.repository;

import com.zima.zimasocial.context.communication.domain.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    void save(Notification notification);
    List<Notification> findAllByIsPushedFalse();
    Optional<Notification> getPreviousNotification(Notification notification);
}
