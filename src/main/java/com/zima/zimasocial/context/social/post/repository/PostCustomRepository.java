package com.zima.zimasocial.context.social.post.repository;

import com.zima.zimasocial.context.social2.api.views.PostDTO;

import java.util.List;

public interface
PostCustomRepository {
    List<PostDTO> findFeed(FeedFilter feedFilter);
}
