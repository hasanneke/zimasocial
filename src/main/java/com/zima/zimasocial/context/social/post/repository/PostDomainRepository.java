package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.views.post.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface
PostDomainRepository {
    Optional<PostDomain> findById(Long postId);
    Page<PostDomain> findAll(Pageable page, String slug, PostCategory type);
    List<PostDTO> findFeed(FeedFilter feedFilter);
    List<PostDTO> findAuthorsPosts(FeedFilter feedFilter);
    void makeInvisiblePostsOfAuthor(AuthorId authorId);
    void makePostsVisibleOfAuthor(AuthorId authorId);
    Page<PostDomain> findFollowingsPosts(Pageable page, AuthorId authorId);
    List<PostDomain> findTodaysPosts();
    PostDomain save(PostDomain post);
    void delete(PostDomain post);
    Long nextSequence();
    List<PostDomain> findAllByAuthorId(AuthorId authorId);
    List<PostDomain> findAllInvisiblePostsByAuthorId(AuthorId authorId);
    List<PostDomain> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
