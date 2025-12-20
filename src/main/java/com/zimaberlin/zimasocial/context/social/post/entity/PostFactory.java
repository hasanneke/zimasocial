package com.zimaberlin.zimasocial.context.social.post.entity;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.entity.PostType;

import java.util.UUID;

public class PostFactory {
    public static Post newPost(Long postId, PostType type, String content, AuthorId authorId, UUID mediaId) {
        return new Post(postId, content, type, authorId, mediaId);
    }
    public static Post newAnyPost(Long postId, String content, AuthorId authorId) {
        return new Post(postId, content, PostType.any, authorId, null);
    }
}
