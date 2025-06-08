package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long postId);
    List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Page<Post> findAll(Pageable page, String slug, PostType type, Sort sort);
    List<Post> findTodaysPosts();
    Post save(Post post);
    void delete(Post post);
}
