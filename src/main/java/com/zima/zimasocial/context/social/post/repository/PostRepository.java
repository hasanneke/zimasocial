package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.views.post.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long postId);
    Page<Post> findAll(Pageable page, String slug, PostCategory type);
    List<PostDTO> findFeed(FeedFilter feedFilter);
    List<PostDTO> findFollowingsFeed(FeedFilter feedFilter) ;
    void makeInvisiblePostsOfAuthor(AuthorId authorId);
    void makePostsVisibleOfAuthor(AuthorId authorId);
    Page<Post> findFollowingsPosts(Pageable page, AuthorId authorId);
    List<Post> findTodaysPosts();
    Post save(Post post);
    void delete(Post post);
    Long nextSequence();
    List<Post> findAllByAuthorId(AuthorId authorId);
    List<Post> findAllInvisiblePostsByAuthorId(AuthorId authorId);
    List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
