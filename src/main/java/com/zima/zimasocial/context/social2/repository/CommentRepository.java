package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findFirstByAuthorIdAndPostIdAndParentIdIsNull(AuthorId authorId, Long postId);
    List<Comment> findByAuthorIdAndPostIdAndParentIdIsNull(AuthorId authorId, Long postId);
    void deleteAllByPostId(Long postId);
}

