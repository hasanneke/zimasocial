package com.zima.zimasocial.context.social2.infastructure;

import com.zima.zimasocial.context.social2.api.views.PostDTO;

import java.util.List;

public interface PostCustomRepository {
    List<PostDTO> findFeed(FeedFilter feedFilter);
}
