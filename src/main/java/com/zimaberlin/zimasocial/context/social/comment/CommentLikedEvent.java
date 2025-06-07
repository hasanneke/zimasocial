package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.author.Author;

public record CommentLikedEvent(Comment comment, Author author) {
}
