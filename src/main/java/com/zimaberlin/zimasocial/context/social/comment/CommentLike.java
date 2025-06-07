package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.values.Like;

public class CommentLike extends Like {
    public CommentLike(Long likeId, Long commentId, Long authorId) {
        super( null, authorId, commentId);
    }
    protected CommentLike(Long commentId, Long authorId) {
        super( null, authorId, commentId);
    }
}
