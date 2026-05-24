package com.zima.zimasocial.context.social.like;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.comment.CommentLike;

import java.util.Optional;

public interface LikeDomainRepository {
    Optional<LikeDomain> findByPostIdAndAuthorId(Long postId, AuthorDomainId authorId);
    Optional<CommentLike> findByCommentIdAndAuthorId(Long commentId, AuthorDomainId authorId);
    void save(LikeDomain like);
    void delete(LikeDomain like);
}
