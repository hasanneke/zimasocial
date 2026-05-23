package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findFirstByUserIdAndPostIdAndParentIdIsNull(Long userId, Long postId);
    List<Comment> findByUserIdAndPostIdAndParentIdIsNull(Long userId, Long postId);
    void deleteAllByPostId(Long postId);
}

