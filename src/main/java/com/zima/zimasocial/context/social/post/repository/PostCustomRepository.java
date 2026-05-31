package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social.post.api.views.PostDTO;
import com.zima.zimasocial.context.social.post.value.FeedFilter;

import java.util.List;

public interface PostCustomRepository {
    List<PostDTO> findFeed(FeedFilter feedFilter);
}
