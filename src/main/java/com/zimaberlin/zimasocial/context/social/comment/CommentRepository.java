package com.zimaberlin.zimasocial.context.social.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
    Page<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);
    Page<Comment> findByPostIdAndParentId(Pageable pageable, Long postId, Long parentId);
}
