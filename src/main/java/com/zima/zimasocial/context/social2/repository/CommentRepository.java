package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.CommentId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, CommentId> {
    List<Comment> findByAuthorIdAndPostIdAndParentIdIsNull(AuthorId authorId, PostId postId);
    @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author author WHERE comment.postId = :postId AND comment.parentId IS NULL ORDER BY comment.createdAt")
    Page<Comment> findByPostIdAndParentIdIsNull(PostId postId, Pageable pageable);
    @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author WHERE comment.parentId = :parentId ORDER BY comment.createdAt")
    Page<Comment> findByParentIdOrderByCreatedAt(CommentId parentId, Pageable pageable);

    @Query(value = "SELECT nextval('comment_id_seq')", nativeQuery = true)
    Long getNextSequenceValue();

    default CommentId getNextId() {
        return new CommentId(getNextSequenceValue());
    }
}

