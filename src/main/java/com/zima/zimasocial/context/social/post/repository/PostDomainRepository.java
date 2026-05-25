package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social2.api.views.PostDTO;
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
    void makeInvisiblePostsOfAuthor(AuthorDomainId authorId);
    void makePostsVisibleOfAuthor(AuthorDomainId authorId);
    Page<PostDomain> findFollowingsPosts(Pageable page, AuthorDomainId authorId);
    List<PostDomain> findTodaysPosts();
    PostDomain save(PostDomain post);
    void delete(PostDomain post);
    Long nextSequence();
    List<PostDomain> findAllByAuthorId(AuthorDomainId authorId);
    List<PostDomain> findAllInvisiblePostsByAuthorId(AuthorDomainId authorId);
    List<PostDomain> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
