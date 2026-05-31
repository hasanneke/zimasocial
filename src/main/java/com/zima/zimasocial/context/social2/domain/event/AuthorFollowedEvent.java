package com.zima.zimasocial.context.social2.domain.event;

import com.zima.zimasocial.context.social2.domain.entity.Author;

public record AuthorFollowedEvent(Author followed, Author follower) {
}
