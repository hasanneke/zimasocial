package com.zimaberlin.zimasocial.context.social.values;
import lombok.Getter;

@Getter
public abstract class Like {
    private Long postId;
    private Long authorId;
    private Long commentId;

    public Like(Long postId, Long authorId, Long commentId) {
        this.postId = postId;
        this.authorId = authorId;
        this.commentId = commentId;
    }
}
