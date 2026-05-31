package com.zima.zimasocial.context.social.author.event;

import com.zima.zimasocial.context.social.author.entity.Author;

public record AuthorFollowedEvent(Author followed, Author follower) {
}
