package com.zimaberlin.zimasocial.context.communication.controller;

import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationReadRepository {
    Page<NotificationView> findByRecipientId(Long recipientId, Pageable pageable);
}
