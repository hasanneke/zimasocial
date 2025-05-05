package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity postEntity);
    Optional<LikeEntity> findByUserAndComment(UserEntity user, CommentEntity comment);

    boolean existsByUserAndPost(UserEntity user,  PostEntity post);
    boolean existsByUserAndComment(UserEntity user, CommentEntity comment);
    Optional<LikeEntity> findByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndPostId(Long userId, Long postId);
}
