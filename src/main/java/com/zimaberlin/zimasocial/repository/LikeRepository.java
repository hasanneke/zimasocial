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

    boolean existsByUserAndPost(UserEntity user,  PostEntity post);
    boolean existsByUserAndPostAndComment(UserEntity user, PostEntity post, CommentEntity comment);
    Optional<LikeEntity> findByUserIdAndPostIdAndCommentId(Long userId, Long postId, Long commentId);
}
