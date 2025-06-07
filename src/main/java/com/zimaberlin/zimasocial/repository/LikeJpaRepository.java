package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByUserIdAndPostId(Long userId,  Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndPostId(Long userId, Long postId);
}
