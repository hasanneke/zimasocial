package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.context.social.values.Like;

public class PostLike extends Like {
    public PostLike(Long postId, Long authorId) {
        super(postId, authorId, null);
    }
}
