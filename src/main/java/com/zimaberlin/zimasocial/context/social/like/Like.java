package com.zimaberlin.zimasocial.context.social.like;
import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import lombok.Getter;

@Getter
public abstract class Like {
    private final Long postId;
    private final AuthorId authorId;
    private final Long commentId;

    public Like(Long postId, AuthorId authorId, Long commentId) {
        this.postId = postId;
        this.commentId = commentId;
        this.authorId = authorId;
    }
}
