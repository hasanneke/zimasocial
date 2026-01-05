package com.zima.zimasocial.context.communication.application;

import com.zima.zimasocial.context.communication.domain.entity.Notification;
import com.zima.zimasocial.context.communication.domain.entity.Recipient;
import com.zima.zimasocial.context.communication.domain.repository.NotificationRepository;
import com.zima.zimasocial.context.communication.domain.repository.RecipientRepository;
import com.zima.zimasocial.context.communication.domain.service.NotificationPolicy;
import com.zima.zimasocial.context.communication.domain.value.DeviceToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationManager {
    private final NotificationRepository notificationRepository;
    private final RecipientRepository recipientRepository;
    private final NotificationPolicy notificationPolicy;
    private final Logger logger = LoggerFactory.getLogger(NotificationManager.class.getName());
    private boolean throttled = false;
    public void sendNotification(Notification notification){
        if(throttled){
            Optional<Notification> checkPreviousNotification = notificationRepository.getPreviousNotification(notification);
            if (checkPreviousNotification.isPresent()) {
                Notification previousNotification = checkPreviousNotification.get();
                if (!notificationPolicy.canResend(previousNotification)) {
                    logger.warn("{} not inserted. Throttled.", notification.getClass().getSimpleName());
                    return;
                }
            }
        }
        notificationRepository.save(notification);
    }
    @Transactional
    public void saveDeviceToken(String token) {
        Recipient recipient = recipientRepository.getAuthenticatedRecipient();
        recipient.addToken(new DeviceToken(token, recipient.getRecipientId()));
        recipientRepository.save(recipient);
    }

    public NotificationManager throttled() {
        NotificationManager notificationManager = new NotificationManager(notificationRepository, recipientRepository, notificationPolicy);
        notificationManager.makeThrottled();
        return notificationManager;
    }

    private void makeThrottled(){
        this.throttled = true;
    }
}
