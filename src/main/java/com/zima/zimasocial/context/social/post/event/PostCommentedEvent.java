package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.post.entity.Comment;
import com.zima.zimasocial.context.social.post.entity.Post;

public record PostCommentedEvent(Post post, Comment comment) {
}
