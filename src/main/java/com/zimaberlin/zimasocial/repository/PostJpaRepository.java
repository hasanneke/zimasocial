package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
    @Query("Select post FROM PostEntity post JOIN UserEntity user ON user.id = post.userId WHERE post.createdAt BETWEEN :start AND :end AND user.isPrivate = false")
    List<PostEntity> findAllByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
    @Query(value = """
             SELECT P.* FROM post P
             INNER JOIN users U ON P.user_id = U.id AND P.is_deleted = false
             INNER JOIN user_relation UR on UR.receiver_id = U.id AND UR.initiated_id = :authorId AND UR.relation = 'followed' AND UR.is_deleted = false
             WHERE P.IS_VISIBLE = TRUE AND P.IS_DELETED = FALSE
            """, nativeQuery = true)
    Page<PostEntity> findFollowingsPosts(Pageable pageable, Long authorId);

    @Query(value = "SELECT nextval('post_sequence')")
    Long getNextSequence();

    @Query(value = """
            SELECT * FROM post WHERE post.is_visible = false AND post.user_id = :userId
            """, nativeQuery = true)
    List<PostEntity> findAllInvisiblePostsByUserId(Long userId);
    List<PostEntity> findAllByUserId(Long userId);
    Optional<PostEntity> findByIdAndIsVisibleTrue(Long id);
}
