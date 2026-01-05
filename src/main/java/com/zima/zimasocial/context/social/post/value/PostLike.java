package com.zima.zimasocial.context.social.post.value;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.like.Like;

public class PostLike extends Like {
    public PostLike(Long postId, AuthorId authorId) {
        super(postId, authorId, null);
    }
}
