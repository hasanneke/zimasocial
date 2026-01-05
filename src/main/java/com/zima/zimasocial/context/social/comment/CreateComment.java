package com.zima.zimasocial.context.social.comment;

import org.springframework.util.Assert;

public record CreateComment(Long postId, Long authorId, String content) {
    @Override
    public Long postId() {
        Assert.notNull(postId, "Post Id cannot be null while creating a comment");
        return postId;
    }
    @Override
    public String content() {
        Assert.notNull(content, "Content cannot be null while creating a comment");
        return content;
    }
}
