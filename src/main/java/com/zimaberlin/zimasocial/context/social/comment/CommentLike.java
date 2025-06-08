package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.like.Like;

public class CommentLike extends Like {
    public CommentLike(Long postId, Long commentId, Long authorId) {
        super( postId, authorId, commentId);
    }
}
