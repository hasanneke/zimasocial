package com.zimaberlin.zimasocial.context.social.like;

import com.zimaberlin.zimasocial.context.social.like.Like;
import com.zimaberlin.zimasocial.entity.LikeType;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository {
    Optional<Like> findByPostIdAndAuthorId(Long postId, Long authorId);
    Optional<Like> findByCommentIdAndAuthorId(Long commentId, Long authorId);
    void save(Like like);
    void delete(Like like);
}
