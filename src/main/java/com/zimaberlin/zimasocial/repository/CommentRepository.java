package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
    Page<CommentEntity> findByParentId(Long parentId, Pageable pageable);
    Page<CommentEntity> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);
    Page<CommentEntity> findByPostIdAndParentId(Pageable pageable, Long postId, Long parentId);
}
