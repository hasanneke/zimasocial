package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;

import com.zimaberlin.zimasocial.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
//    Page<PostEntity> findByOrderByCreatedAt(Pageable pageable);
    Page<PostEntity> findByType(Pageable page,  PostType type);
    Page<PostEntity> findByUserOrderByCreatedAt(Pageable page,  UserEntity user);
    Page<PostEntity> findByUserAndTypeOrderByCreatedAt(Pageable page,  UserEntity user, PostType type);
}
