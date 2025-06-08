package com.zimaberlin.zimasocial.context.social.like;

import com.zimaberlin.zimasocial.context.social.like.Like;

import java.util.Optional;

public interface LikeRepository {
    Optional<Like> findByPostIdAndAuthorId(Long postId, Long authorId);
    Optional<Like> findByCommentIdAndAuthorId(Long commentId, Long authorId);
    void save(Like like);
    void delete(Like like);
}
