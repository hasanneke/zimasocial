package com.zima.zimasocial.context.social.post.value;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.like.LikeDomain;

public class PostLike extends LikeDomain {
    public PostLike(Long postId, AuthorDomainId authorId) {
        super(postId, authorId, null);
    }
}
