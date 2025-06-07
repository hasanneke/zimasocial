package com.zimaberlin.zimasocial.context.social.post;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
public class Post {
    private Long postId;
    private String content;
    private String url;
    private PostType type;
    private int likeCount;
    private int commentCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final Long authorId;

    public Post(Long postId, String content, PostType type, int likeCount, int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, Long authorId) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.notNull(type, "Post type cannot be null");
        Assert.isTrue(likeCount >= 0, "Like count cannot be negative");
        Assert.isTrue(commentCount >= 0, "Comment count cannot be negative");
        this.postId = postId;
        this.content = content;
        this.type = type;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
    }

    protected Post(String content, PostType type, Long authorId) {
        this.content = content;
        this.type = type;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = LocalDateTime.now();
        this.authorId = authorId;
    }
    public void like(Author liker) {
        likeCount = likeCount + 1;
        StaticEventPublisher.publishEvent(new PostLikedEvent(postId, liker.getAuthorId()));
    }
    public void commented(Author commenter) {
        commentCount = commentCount + 1;
        StaticEventPublisher.publishEvent(new PostCommentedEvent(postId, commenter.getAuthorId()));
    }
    public void commentRemoved() {
        commentCount = commentCount - 1;
    }
    public void unliked() {
        likeCount = likeCount - 1;
        updatedAt = LocalDateTime.now();
    }
}
