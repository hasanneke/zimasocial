package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.LikeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByUserIdAndPostId(Long userId,  Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndPostIdAndType(Long userId, Long postId, LikeType type);
}