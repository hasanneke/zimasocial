package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostPostEntityAdapter {
    private final AuthorUserEntityAdapter authorUserEntityAdapter;

    @Autowired
    public PostPostEntityAdapter(AuthorUserEntityAdapter authorUserEntityAdapter) {
        this.authorUserEntityAdapter = authorUserEntityAdapter;
    }

    public Post convertPostEntityToPost(PostEntity post) {
        return new Post(post.getId(), post.getContent(), post.getType(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(),  post.getUser().getId());
    }
}
