package com.zima.zimasocial.context.social.post.entity;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.comment.Comment;
import com.zima.zimasocial.context.social.post.event.PostLikedEvent;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
public class Post {
    private Long postId;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AuthorId authorId;
    private PostContent content;
    private Boolean isVisible;
    private Integer score;
    private LocalDateTime lastPunishedAt;
    private Post(Long postId) {
        this.postId = Objects.requireNonNull(postId, "Post id cannot be null");
    }

    public static Post create(
            Long postId,
            AuthorId authorId,
            PostContent content,
            Clock clock
    ) {
        Assert.notNull(authorId, "Author is required");

        LocalDateTime now = LocalDateTime.now(clock);

        Post post = new Post(postId);
        post.authorId = authorId;
        post.content = content;

        post.likeCount = 0;
        post.commentCount = 0;
        post.score = 100;
        post.isVisible = true;

        post.createdAt = now;
        post.updatedAt = null;
        post.lastPunishedAt = null;

        return post;
    }

    public static Post reconstitute(
            Long postId,
            AuthorId authorId,
            PostContent content,
            int likeCount,
            int commentCount,
            int score,
            boolean visible,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastPunishedAt
    ) {
        Post post = new Post(postId);
        post.authorId = authorId;
        post.content = content;
        post.likeCount = likeCount;
        post.commentCount = commentCount;
        post.score = score;
        post.isVisible = visible;
        post.createdAt = createdAt;
        post.updatedAt = updatedAt;
        post.lastPunishedAt = lastPunishedAt;

        return post;
    }

    public PostLike like(AuthorId likerAuthorId) {
        likeCount += 1;
        if(!authorId.equals(likerAuthorId)){
            score += 2;
        }
        StaticEventPublisher.publishEvent(new PostLikedEvent(postId, authorId, likerAuthorId));
        return new PostLike(postId, likerAuthorId);
    }
    public Comment comment(AuthorId commenterAuthorId, String comment) {
        commentCount += 1;
        if(!authorId.equals(commenterAuthorId)){
            score += 5;
        }
        return new Comment(postId, null, commenterAuthorId, comment);
    }
    public void removeComment(AuthorId commenterId) {
        commentCount = commentCount - 1;
        if(!authorId.equals(commenterId)){
            score -= 5;
        }
    }
    public void unliked(AuthorId unlikerAuthorId) {
        likeCount = likeCount - 1;
        if(!authorId.equals(unlikerAuthorId)){
            score -= 2;
        }
    }

    public void punishScore() {
        if(!isPunishable()) return;
        long hoursPassedSinceLastPunishment = ChronoUnit.HOURS.between(lastPunishedAt, OffsetDateTime.now());
        for (long i = 0; i < hoursPassedSinceLastPunishment; i++) {
            punish();
        }
        this.lastPunishedAt = LocalDateTime.now();
    }

    private void punish() {
        score -= (int) (score * 0.04);
    }

    private boolean isPunishable() {
        long passedHoursFromCreation = ChronoUnit.HOURS.between(createdAt, OffsetDateTime.now());
        return !(passedHoursFromCreation > 72 && getScore() < 100);
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
