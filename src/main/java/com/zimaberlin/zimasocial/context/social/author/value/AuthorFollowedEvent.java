package com.zimaberlin.zimasocial.context.social.author.value;

import com.zimaberlin.zimasocial.context.social.author.entity.Author;

public record AuthorFollowedEvent(Author followed, Author follower) {
}
