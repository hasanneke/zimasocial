package com.zima.zimasocial.repository;

import com.zima.zimasocial.context.social.api.dto.LikeDTO;
import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.entity.LikeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByUserIdAndPostId(Long userId,  Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndCommentId(Long userId, Long commentId);
    Optional<LikeEntity> findByUserIdAndPostIdAndType(Long userId, Long postId, LikeType type);
    @Query("""
        SELECT new com.zima.zimasocial.context.social.api.dto.LikeDTO(
                liker.slug,
                liker.name fullName,
                liker.avatarFileName,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM UserRelationEntity relation
                                 WHERE relation.relation = com.zima.zimasocial.entity.userRelation.Relation.followed
                                 AND relation.actorId = :readerId AND relation.receiverId = liker.id
                )THEN true ELSE false END,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM UserRelationEntity relation
                                 WHERE relation.relation = com.zima.zimasocial.entity.userRelation.Relation.followed
                                 AND relation.receiverId = :readerId AND relation.actorId = liker.id
                ) THEN true ELSE false END,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM FollowRequestEntity followRequest
                                 WHERE followRequest.followerId = :readerId
                                 AND followRequest.followedId = liker.id
                ) THEN true ELSE false END,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM FollowRequestEntity followRequest
                                 WHERE followRequest.followerId = liker.id
                                 AND followRequest.followedId = :readerId
                ) THEN true ELSE false END,
               liker.isPrivate)
            FROM LikeEntity likeEntity
        JOIN PostJpaEntity post ON post.id = likeEntity.postId
        JOIN UserEntity user ON user.id = post.userId
        JOIN UserEntity liker ON liker.id = likeEntity.userId
        WHERE (likeEntity.postId = :resourceId AND likeEntity.commentId IS NULL)
        AND liker.isDisabled = false
        AND liker.isBanned = false
        AND likeEntity.userId != :readerId
    """)
    Page<LikeDTO> findAllLikes(Long resourceId, Long readerId, Pageable pageable);
}