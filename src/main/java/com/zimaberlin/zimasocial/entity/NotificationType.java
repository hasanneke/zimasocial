package com.zimaberlin.zimasocial.entity;

import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(value = "IS_DELETED IS FALSE")
public enum NotificationType {
    POST_LIKED,
    COMMENT_LIKED,
    USER_FOLLOWED_YOU,
    POST_COMMENTED,
    COMMENT_REPLIED,
    POST_DELETED,
    POST_SHARED,
    WELCOME,
    NEWS,
    IMPORTANT,
    VERY_IMPORTANT,
    DANGER
}
