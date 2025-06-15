package com.zimaberlin.zimasocial.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StaticEventPublisher {
    private static ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public StaticEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        StaticEventPublisher.applicationEventPublisher = applicationEventPublisher;
    }
    public static void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
