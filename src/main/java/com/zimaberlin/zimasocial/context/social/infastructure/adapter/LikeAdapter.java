package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.post.PostLike;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.context.social.values.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeAdapter {
    public Like convertLikeEntityToLikeForPost(LikeEntity like) {
        return new PostLike(like.getPostId(), like.getPostId());
    }
}
