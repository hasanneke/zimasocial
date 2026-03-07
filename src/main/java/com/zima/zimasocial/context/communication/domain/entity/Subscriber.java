package com.zima.zimasocial.context.communication.domain.entity;

import com.zima.zimasocial.context.communication.domain.value.DeviceToken;
import lombok.Getter;

import java.util.List;

@Getter
public class Subscriber {
    private List<DeviceToken> deviceTokens;
    public Subscriber(List<DeviceToken> deviceTokens) {
        this.deviceTokens = deviceTokens;
    }
}
