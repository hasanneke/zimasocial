package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long postId);
    Page<Post> findByType(Pageable page, PostType type);
    Page<Post> findByUserOrderByCreatedAt(Pageable page,  UserEntity user);
    Page<Post> findByUserAndTypeOrderByCreatedAt(Pageable page,  UserEntity user, PostType type);
    List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Post save(Post post);
    void delete(Post post);
}
