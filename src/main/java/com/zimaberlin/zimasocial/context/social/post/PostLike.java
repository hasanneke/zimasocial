package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.like.Like;

public class PostLike extends Like {
    public PostLike(Long postId, AuthorId authorId) {
        super(postId, authorId, null);
    }
}
