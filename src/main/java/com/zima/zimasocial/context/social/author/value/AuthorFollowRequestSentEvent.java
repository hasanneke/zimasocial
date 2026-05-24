package com.zima.zimasocial.context.social.author.value;

public record AuthorFollowRequestSentEvent(AuthorDomainId followerId, AuthorDomainId followedId) {
}
