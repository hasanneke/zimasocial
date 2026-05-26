package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.api.post.PostCategory;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social2.api.views.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface
PostCustomRepository {
    public Page<PostDomain> findAll(Pageable page, String slug, PostCategory category);
    public List<PostDTO> findAuthorsPosts(FeedFilter feedFilter);
    List<PostDTO> findFeed(FeedFilter feedFilter);
}
