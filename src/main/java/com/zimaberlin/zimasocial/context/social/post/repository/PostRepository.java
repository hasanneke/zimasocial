package com.zimaberlin.zimasocial.context.social.post.repository;

import com.zimaberlin.zimasocial.context.social.api.post.PostCategory;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long postId);
    Page<Post> findAll(Pageable page, String slug, PostCategory type);
    void makeInvisiblePostsOfAuthor(AuthorId authorId);
    void makePostsVisibleOfAuthor(AuthorId authorId);
    Page<Post> findFollowingsPosts(Pageable page, AuthorId authorId);
    List<Post> findTodaysPosts();
    Post save(Post post);
    void delete(Post post);
    Long nextSequence();
    List<Post> findAllByAuthorId(AuthorId authorId);
    List<Post> findAllInvisiblePostsByAuthorId(AuthorId authorId);
    List<Post> findAllByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
}
