package com.zima.zimasocial.context.social.author.value;

import com.zima.zimasocial.context.social.author.entity.Author;

public record AuthorFollowedEvent(Author followed, Author follower) {
}
