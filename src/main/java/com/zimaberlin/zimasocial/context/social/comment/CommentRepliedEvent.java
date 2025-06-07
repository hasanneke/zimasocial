package com.zimaberlin.zimasocial.context.social.comment;

public record CommentRepliedEvent(Comment parent, Comment reply) {
}
