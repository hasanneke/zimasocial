package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.like.Like;

public class CommentLike extends Like {
    public CommentLike(Long postId, AuthorId authorId, Long commentId) {
        super( postId, authorId, commentId) ;
    }
}
