package com.zima.zimasocial.context.communication.batch;

import com.zima.zimasocial.context.communication.application.PushNotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushNotificationsRunnable implements Runnable {

    private final PushNotificationService pushNotificationService;

    Logger logger = LoggerFactory.getLogger(PushNotificationsRunnable.class);
    @Override
    public void run() {
        pushNotificationService.startPushing();
    }
}
