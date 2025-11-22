package com.zimaberlin.zimasocial.context.communication.application;

import com.google.firebase.messaging.FirebaseMessagingException;

public interface PushNotificationProvider {
    void push(PushNotification pushNotification) throws FirebaseMessagingException;
}
