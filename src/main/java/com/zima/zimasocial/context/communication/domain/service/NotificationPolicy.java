package com.zima.zimasocial.context.communication.domain.service;

import com.zima.zimasocial.context.communication.domain.entity.Notification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class NotificationPolicy {
    private final Duration throttle = Duration.ofHours(2);
    public boolean canResend(Notification notification){
        return ChronoUnit.HOURS.between(notification.getCreatedAt(), OffsetDateTime.now()) >= 2;
    }
}
