package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
    Page<CommentEntity> findByParentId(Long parentId, Pageable pageable);
    Page<CommentEntity> findByParentIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    Page<CommentEntity> findByPostIdAndParentId(Pageable pageable, Long postId, Long parentId);
    Page<CommentEntity> findByPostIdOrderByCreatedAtDesc(Pageable pageable, Long postId, Long parentId);
    void deleteAllByUser(UserEntity user);
    List<CommentEntity> findAllByUser(UserEntity user);
}
