package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;

public class PostMapper {
    public static Post PostEntityToPost(PostEntity postEntity){
        return Post.builder()
                .id(postEntity.getId())
                .likeCount(postEntity.getLikeCount())
                .commentCount(postEntity.getCommentCount())
                .url(postEntity.getUrl())
                .type(postEntity.getType())
                .user(postEntity.getUser())
                .build();
    }
}
