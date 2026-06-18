package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.post.api.views.LikeView;
import com.zima.zimasocial.context.social.post.entity.Like;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.context.social.post.value.LikeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByAuthorIdAndPostIdAndType(AuthorId authorId, PostId postId, LikeType type);
    boolean existsByAuthorIdAndPostIdAndType(AuthorId authorId, PostId postId, LikeType type);
    Optional<Like> findByAuthorIdAndCommentIdAndType(AuthorId authorId, CommentId commentId, LikeType type);

    @Query("""
        SELECT new com.zima.zimasocial.context.social.post.api.views.LikeView(
                liker.slug,
                liker.name fullName,
                liker.avatarFileName,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM AuthorRelation relation
                                 WHERE relation.relation = com.zima.zimasocial.context.social.author.value.Relation.followed
                                 AND relation.actorId = :readerId AND relation.receiverId = liker.id
                )THEN true ELSE false END,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM AuthorRelation relation
                                 WHERE relation.relation = com.zima.zimasocial.context.social.author.value.Relation.followed
                                 AND relation.receiverId = :readerId AND relation.actorId = liker.id
                ) THEN true ELSE false END,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM FollowRequest followRequest
                                 WHERE followRequest.followerId = :readerId
                                 AND followRequest.followedId = liker.id
                ) THEN true ELSE false END,
                CASE WHEN EXISTS (
                                 SELECT 1 FROM FollowRequest followRequest
                                 WHERE followRequest.followerId = liker.id
                                 AND followRequest.followedId = :readerId
                ) THEN true ELSE false END,
               liker.isPrivate)
            FROM Like like
        JOIN Post post ON post.id = like.postId
        JOIN Author user ON user.id = post.authorId
        JOIN Author liker ON liker.id = like.authorId
        WHERE (like.postId = :resourceId AND like.commentId IS NULL)
        AND liker.isDisabled = false
        AND liker.isBanned = false
        AND like.authorId != :readerId
    """)
    Page<LikeView> findAllLikes(Long resourceId, AuthorId readerId, Pageable pageable);
}
