package com.zimaberlin.zimasocial.context.communication;

import lombok.Getter;

@Getter
public class PushNotification {
    private String title;
    private String message;
    private String deviceToken;
    private String topic;

    public PushNotification(String title, String message, String deviceToken) {
        this.title = title;
        this.message = message;
        this.deviceToken = deviceToken;
    }
    public PushNotification( String message, String deviceToken) {
        this.message = message;
        this.deviceToken = deviceToken;
    }
}
