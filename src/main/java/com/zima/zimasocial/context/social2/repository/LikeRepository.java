package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social2.domain.entity.Like;
import com.zima.zimasocial.entity.LikeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByAuthorIdAndPostIdAndType(Long userId, Long postId, LikeType type);
}
