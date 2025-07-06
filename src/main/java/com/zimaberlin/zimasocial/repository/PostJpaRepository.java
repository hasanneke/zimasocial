package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
//    Page<PostEntity> findByOrderByCreatedAt(Pageable pageable);
    Page<PostEntity> findAllByOrderByCreatedAtDesc(Specification<PostEntity> specification, Pageable pageable);
    Page<PostEntity> findByType(Pageable page,  PostType type);
    Page<PostEntity> findByUserOrderByCreatedAt(Pageable page,  UserEntity user);
    Page<PostEntity> findByUserAndTypeOrderByCreatedAt(Pageable page,  UserEntity user, PostType type);
    List<PostEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    void deleteAllByUser(UserEntity user);
    List<PostEntity> findAllByUser(UserEntity user);

    @Query(value = """
              SELECT P.* FROM post P
             INNER JOIN users U ON P.user_id = U.id AND P.is_deleted = false
             INNER JOIN user_relation UR on UR.receiver_id = U.id AND UR.initiated_id = 2 AND UR.relation = 'followed' AND UR.is_deleted = false
             WHERE P.IS_DELETED = FALSE
            """, nativeQuery = true)
    Page<PostEntity> findFollowingsPosts(Pageable pageable, Long authorId);
}
