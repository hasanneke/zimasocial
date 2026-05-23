package com.zima.zimasocial.context.social.like;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import lombok.Getter;

@Getter
public abstract class LikeDomain {
    private final Long postId;
    private final AuthorId authorId;
    private final Long commentId;

    public LikeDomain(Long postId, AuthorId authorId, Long commentId) {
        this.postId = postId;
        this.commentId = commentId;
        this.authorId = authorId;
    }
}
