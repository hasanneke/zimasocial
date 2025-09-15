package com.zimaberlin.zimasocial.context.social.author;

public record AuthorFollowRequestSentEvent(AuthorId followerId, AuthorId followedId) {
}
