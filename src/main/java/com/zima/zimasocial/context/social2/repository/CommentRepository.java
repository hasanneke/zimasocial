package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findFirstByAuthorIdAndPostIdAndParentIdIsNull(AuthorId authorId, Long postId);
    List<Comment> findByAuthorIdAndPostIdAndParentIdIsNull(AuthorId authorId, Long postId);
    @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author author WHERE comment.postId = :postId AND comment.parentId IS NULL ORDER BY comment.createdAt")
    Page<Comment> findByPostIdAndParentIdIsNull(Long postId, Pageable pageable);
    @Query("SELECT comment FROM Comment comment JOIN FETCH comment.author WHERE comment.parentId = :parentId ORDER BY comment.createdAt")
    Page<Comment> findByParentIdOrderByCreatedAt(Long parentId, Pageable pageable);
    void deleteAllByPostId(Long postId);
}

