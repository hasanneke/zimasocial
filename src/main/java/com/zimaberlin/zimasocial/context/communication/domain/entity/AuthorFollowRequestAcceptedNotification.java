package com.zimaberlin.zimasocial.context.communication.domain.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class AuthorFollowRequestAcceptedNotification extends Notification{
    private String slug;
}
