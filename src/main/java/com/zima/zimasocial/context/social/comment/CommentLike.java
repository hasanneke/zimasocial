package com.zima.zimasocial.context.social.comment;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.like.LikeDomain;

public class CommentLike extends LikeDomain {
    public CommentLike(Long postId, AuthorDomainId authorId, Long commentId) {
        super( postId, authorId, commentId) ;
    }
}
