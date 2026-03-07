package com.zima.zimasocial.context.communication.application;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PushNotification {
    private final String message;
    private final String deviceToken;
    private final List<String> deviceTokens;
    private String title;
    private String type;
    private String linkToSource;
    private String resourceId;
}
