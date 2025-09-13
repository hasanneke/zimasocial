package com.zimaberlin.zimasocial.context.social.post;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Post {
    private Long postId;
    private String content;
    private PostType type;
    private int likeCount;
    private int commentCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final AuthorId authorId;
    private MovieMedia movie;
    private BookMedia book;
    private Boolean isVisible;

    public Post(Long postId, String content, int likeCount, int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, AuthorId authorId) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.isTrue(likeCount >= 0, "Like count cannot be negative");
        Assert.isTrue(commentCount >= 0, "Comment count cannot be negative");
        this.postId = postId;
        this.content = content;
        this.type = PostType.any;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.isVisible = true;
    }
    public Post(Long postId, String content, int likeCount, int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, AuthorId authorId, MovieMedia movie) {
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
        this.isVisible = true;
    }

    public Post(Long postId, String content, int likeCount, int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt, AuthorId authorId, BookMedia book) {
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
        this.isVisible = true;
    }
    protected Post( String content, PostType type, AuthorId authorId) {
        this.content = content;
        this.type = type;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = LocalDateTime.now();
        this.authorId = authorId;
        this.isVisible = true;
    }
    public Post(Long postId, String content, PostType type, AuthorId authorId) {
        this.postId = postId;
        this.content = content;
        this.type = type;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = LocalDateTime.now();
        this.authorId = authorId;
        this.isVisible = true;
    }
    public Post(Long postId, String content, AuthorId authorId, BookMedia book) {
        this.postId = postId;
        this.content = content;
        this.type = PostType.book;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = LocalDateTime.now();
        this.authorId = authorId;
        this.book = book;
        this.isVisible = true;
    }
    public Post(Long postId, String content, AuthorId authorId, MovieMedia movie) {
        this.postId = postId;
        this.content = content;
        this.type = PostType.movie;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = LocalDateTime.now();
        this.authorId = authorId;
        this.movie = movie;
        this.isVisible = true;
    }
    public PostLike like(AuthorId likerAuthorId) {
        likeCount += 1;
        StaticEventPublisher.publishEvent(new PostLikedEvent(postId, authorId, likerAuthorId));
        return new PostLike(postId, likerAuthorId);
    }
    public Comment comment(AuthorId commenterAuthorId, String comment) {
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
    public void makeInvisible() {
        this.isVisible = false;
    }
    public void makeVisible() {
        this.isVisible = true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }
}
