package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
//    Page<PostEntity> findByOrderByCreatedAt(Pageable pageable);
    Page<PostEntity> findByType(Pageable page,  PostType type);
    Page<PostEntity> findByUserOrderByCreatedAt(Pageable page,  UserEntity user);
    Page<PostEntity> findByUserAndTypeOrderByCreatedAt(Pageable page,  UserEntity user, PostType type);
    List<PostEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    void deleteAllByUser(UserEntity user);
    List<PostEntity> findAllByUser(UserEntity user);
}
