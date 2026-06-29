package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.post.entity.Comment;

public record CommentRepliedEvent(Comment parent, Comment reply) {
}
