package com.zimaberlin.zimasocial.context.social.repository;

import com.zimaberlin.zimasocial.context.social.values.Like;

import java.util.Optional;

public interface LikeRepository {
    Optional<Like> findByPostIdAndAuthorId(Long postId, Long authorId);
    void save(Like like);
    void delete(Like like);
}
