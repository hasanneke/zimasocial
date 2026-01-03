package com.zimaberlin.zimasocial.context.social.like;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.CommentLike;

import java.util.Optional;

public interface LikeRepository {
    Optional<Like> findByPostIdAndAuthorId(Long postId, AuthorId authorId);
    Optional<CommentLike> findByCommentIdAndAuthorId(Long commentId, AuthorId authorId);
    void save(Like like);
    void delete(Like like);
}
