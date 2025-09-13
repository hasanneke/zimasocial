package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.entity.PostType;

public class PostFactory {
    private PostRepository postRepository;
    public Post createAnyPost(String content, PostType type, AuthorId authorId) {
        Long postId = postRepository.nextSequence();
        return new Post(postId, content, type, authorId);
    }
}
