package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT commentEntity FROM CommentEntity commentEntity JOIN FETCH commentEntity.user user WHERE commentEntity.postId = :postId AND commentEntity.parentId IS NULL AND user.isDisabled = false AND user.isBanned = false ORDER BY commentEntity.createdAt")
    Page<CommentEntity> findByPostIdAndParentIdIsNull(Long postId, Pageable pageable);
    @Query("SELECT commentEntity FROM CommentEntity commentEntity JOIN FETCH commentEntity.user WHERE commentEntity.parentId = :parentId ORDER BY commentEntity.createdAt")
    Page<CommentEntity> findByParentIdOrderByCreatedAt(Long parentId, Pageable pageable);
    void deleteByParentId(Long parentId);
}
