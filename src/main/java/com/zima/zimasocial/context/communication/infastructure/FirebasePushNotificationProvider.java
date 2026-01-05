package com.zima.zimasocial.context.communication.infastructure;
import com.google.firebase.messaging.*;
import com.zima.zimasocial.context.communication.application.PushNotification;
import com.zima.zimasocial.context.communication.application.PushNotificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FirebasePushNotificationProvider implements PushNotificationProvider {
    public void push(PushNotification pushNotification)  throws FirebaseMessagingException{
        Notification notification = Notification.builder()
                                .setTitle(pushNotification.getTitle())
                                .setBody(pushNotification.getMessage())
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(pushNotification.getDeviceToken())
                .setNotification(notification)
                .putData("url", pushNotification.getLinkToSource())
                .build();
        FirebaseMessaging.getInstance().send(message);
    }
}
