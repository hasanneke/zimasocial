package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.Post;
import com.zimaberlin.zimasocial.entity.PostType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByType(Pageable page, PostType type);
}
