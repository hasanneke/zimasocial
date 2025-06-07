package com.zimaberlin.zimasocial.context.social.api;

import com.zimaberlin.zimasocial.context.social.post.CreatePost;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostService;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.views.post.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class PostControllerBridge {
    private final PostService postService;
    @Autowired
    public PostControllerBridge(PostService postService) {
        this.postService = postService;
    }

    public PostView createPost(PostPayload payload) {
        Post post = postService.createPost(
                CreatePost.builder()
                        .type(payload.getType())
                        .content(payload.getContent())
                        .build()
        );
        return new PostView();
    }
}
