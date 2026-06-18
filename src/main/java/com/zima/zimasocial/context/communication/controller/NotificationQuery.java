package com.zima.zimasocial.context.communication.controller;

import com.zima.zimasocial.context.communication.controller.views.NotificationView;
import com.zima.zimasocial.context.communication.controller.views.NotificationView2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQuery {
    Page<NotificationView> findByRecipientId(Long recipientId, Pageable pageable);
    Page<NotificationView2> findByRecipientIdV2(Long recipientId, Pageable pageable);
}
