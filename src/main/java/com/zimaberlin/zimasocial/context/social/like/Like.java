package com.zimaberlin.zimasocial.context.social.like;
import lombok.Getter;

@Getter
public abstract class Like {
    private final Long postId;
    private final Long authorId;
    private final Long commentId;

    public Like(Long postId, Long authorId, Long commentId) {
        this.postId = postId;
        this.commentId = commentId;
        this.authorId = authorId;
    }
}
