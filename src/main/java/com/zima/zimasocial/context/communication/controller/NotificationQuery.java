package com.zima.zimasocial.context.communication.controller;

import com.zima.zimasocial.views.notification.NotificationView2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQuery {
    Page<NotificationView2> findByRecipientId(Long recipientId, Pageable pageable);
}
