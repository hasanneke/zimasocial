package com.zima.zimasocial.context.social.like;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import lombok.Getter;

@Getter
public abstract class LikeDomain {
    private final Long postId;
    private final AuthorDomainId authorId;
    private final Long commentId;

    public LikeDomain(Long postId, AuthorDomainId authorId, Long commentId) {
        this.postId = postId;
        this.commentId = commentId;
        this.authorId = authorId;
    }
}
