package com.zima.zimasocial.context.social.comment;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.like.Like;

public class CommentLike extends Like {
    public CommentLike(Long postId, AuthorId authorId, Long commentId) {
        super( postId, authorId, commentId) ;
    }
}
