package com.zima.zimasocial.context.social.author.value;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;

public record AuthorFollowedEvent(AuthorDomain followed, AuthorDomain follower) {
}
