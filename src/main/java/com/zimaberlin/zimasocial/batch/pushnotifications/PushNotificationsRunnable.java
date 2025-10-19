package com.zimaberlin.zimasocial.batch.pushnotifications;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.zimaberlin.zimasocial.context.communication.NotificationRepository;
import com.zimaberlin.zimasocial.context.communication.PushNotificationService;
import com.zimaberlin.zimasocial.context.communication.RecipientValidator;
import com.zimaberlin.zimasocial.context.communication.domain.DeviceToken;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.notifications.Notification;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PushNotificationsRunnable implements Runnable {
    private final NotificationRepository notificationRepository;
    private final RecipientRepository recipientRepository;
    private final PushNotificationService pushNotificationService;
    private final RecipientValidator recipientValidator;
    Logger logger = LoggerFactory.getLogger(PushNotificationsRunnable.class);
    @Override
    public void run() {
        List<Notification> notificationList = notificationRepository.findAllByIsPushedFalse();
        for (Notification notification : notificationList) {
            Optional<Recipient> recipient = recipientRepository.findByRecipientId(notification.getRecipientId());
            if (recipient.isPresent()){
                try {
                    boolean eligibleForPush = recipientValidator.canBePushed(recipient.get().getRecipientId());
                    if(eligibleForPush){
                        Recipient getRecipient = recipient.get();
                        Set<DeviceToken> deviceTokens = getRecipient.getDeviceTokens();
                        pushNotificationService.push(notification, deviceTokens.stream().toList());
                    }
                } catch (FirebaseMessagingException e) {
                    if(e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)){
                        notification.push();
                    }
                }
                notification.push();
                notificationRepository.save(notification);
            }
        }
    }
}
