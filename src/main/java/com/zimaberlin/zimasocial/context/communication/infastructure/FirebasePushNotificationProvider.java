package com.zimaberlin.zimasocial.context.communication.infastructure;

import com.google.firebase.messaging.*;
import com.zimaberlin.zimasocial.context.communication.application.PushNotification;
import com.zimaberlin.zimasocial.context.communication.application.PushNotificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirebasePushNotificationProvider implements PushNotificationProvider {
    public void push(PushNotification pushNotification)  {
        Notification notification = Notification.builder()
                                .setTitle(pushNotification.getTitle())
                                .setBody(pushNotification.getMessage())
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(pushNotification.getDeviceToken())
                .setNotification(notification)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
