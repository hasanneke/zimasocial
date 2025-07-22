package com.zimaberlin.zimasocial.context.social.post;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.media.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
public class Post {
    private Long postId;
    private String content;
    private PostType type;
    private int likeCount;
    private int commentCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final Long authorId;
    private MovieMedia movie;
    private BookMedia book;

    public Post(Long postId, String content, int likeCount, int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, Long authorId, MovieMedia movie) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.isTrue(likeCount >= 0, "Like count cannot be negative");
        Assert.isTrue(commentCount >= 0, "Comment count cannot be negative");
        this.postId = postId;
        this.content = content;
        this.type = PostType.movie;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.movie = movie;
    }

    public Post(Long postId, String content, int likeCount, int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, Long authorId, BookMedia book) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.isTrue(likeCount >= 0, "Like count cannot be negative");
        Assert.isTrue(commentCount >= 0, "Comment count cannot be negative");
        this.postId = postId;
        this.content = content;
        this.type = PostType.book;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.book = book;
    }
    protected Post(String content, PostType type, Long authorId) {
        this.content = content;
        this.type = type;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = LocalDateTime.now();
        this.authorId = authorId;
    }
    public PostLike like(Long likerAuthorId) {
        likeCount += 1;
        StaticEventPublisher.publishEvent(new PostLikedEvent(postId, authorId, likerAuthorId));
        return new PostLike(postId, likerAuthorId);
    }
    public Comment comment(Long commenterAuthorId, String comment) {
        commentCount += 1;

        return new Comment(postId, null, commenterAuthorId, comment);
    }
    public void removeComment(Long commentId) {
        commentCount = commentCount - 1;
    }
    public void unliked() {
        likeCount = likeCount - 1;
        updatedAt = LocalDateTime.now();
    }
    public void setMovie(MovieMedia movie) {
        this.movie = movie;
    }
    public void setBook(BookMedia book){
        this.book = book;
    }
}
