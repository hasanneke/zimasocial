package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
public class Comment {
    private Long commentId;
    private Long parentCommentId;
    private Long postId;
    private Long authorId;
    private String content;
    private int likeCount;
    private int replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(Long commentId, Long parentCommentId, Long postId, Long authorId, String content, int likeCount, int replyCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Assert.notNull(commentId, "Comment Id cannot be null");
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.notNull(authorId, "Author Id cannot be null");
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Comment(Long postId, Long parentCommentId, Long authorId, String content) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.notNull(authorId, "Author Id cannot be null");
        this.parentCommentId = parentCommentId;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.likeCount = 0;
        this.replyCount = 0;
    }
    public Comment reply(Long replierAuthorId, String comment){
        replyCount += 1;
        return new Comment(postId, commentId, replierAuthorId, comment);
    }
    public void removeReply(Comment reply) {
        replyCount -= 1;
    }

    public CommentLike like(Author liker){
        likeCount += 1;
        StaticEventPublisher.publishEvent(new CommentLikedEvent(this.postId, this.getCommentId(), authorId, liker.getAuthorId()));
        return new CommentLike(postId, liker.getAuthorId(), commentId);
    }

    public void unlike(){
        likeCount -= 1;
    }
}
