package com.zimaberlin.zimasocial.context.social.like;
import lombok.Getter;

@Getter
public abstract class Like {
    private Long postId;
    private Long authorId;
    private Long commentId;

    public Like(Long postId, Long commentId, Long authorId) {
        this.postId = postId;
        this.commentId = commentId;
        this.authorId = authorId;
    }
}
