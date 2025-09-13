package com.zimaberlin.zimasocial.context.social.like;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.CommentLike;
import com.zimaberlin.zimasocial.context.social.like.Like;
import com.zimaberlin.zimasocial.entity.LikeType;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository {
    Optional<Like> findByPostIdAndAuthorId(Long postId, AuthorId authorId);
    Optional<CommentLike> findByCommentIdAndAuthorId(Long commentId, AuthorId authorId);
    void save(Like like);
    void delete(Like like);
}
