package com.zima.zimasocial.context.communication.application;

import lombok.Getter;

@Getter
public class PushNotification {
    private final String message;
    private final String deviceToken;
    private String title;
    private String linkToSource;
    public PushNotification(String message, String deviceToken) {
        this.message = message;
        this.deviceToken = deviceToken;
    }
    public PushNotification( String message, String deviceToken, String linkToSource) {
        this.message = message;
        this.deviceToken = deviceToken;
        this.linkToSource = linkToSource;
    }
}
