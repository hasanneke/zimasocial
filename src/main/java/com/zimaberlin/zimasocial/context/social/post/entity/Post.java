package com.zimaberlin.zimasocial.context.social.post.entity;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.post.event.PostLikedEvent;
import com.zimaberlin.zimasocial.context.social.post.value.PostLike;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long postId;
    private String content;
    private PostType type;
    private int likeCount;
    private int commentCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private AuthorId authorId;
    private UUID mediaId;
    private Boolean isVisible;
    private Integer score;
    private OffsetDateTime lastPunishedAt;

    public Post(Long postId, PostType postType, UUID mediaId, String content, int likeCount, int commentCount, OffsetDateTime createdAt, OffsetDateTime updatedAt, AuthorId authorId, Integer score, OffsetDateTime lastPunishedTime) {
        Assert.notNull(postId, "Post Id cannot be null");
        Assert.isTrue(likeCount >= 0, "Like count cannot be negative");
        Assert.isTrue(commentCount >= 0, "Comment count cannot be negative");
        this.postId = postId;
        this.content = content;
        this.type = postType;
        this.mediaId = mediaId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.score = score;
        this.isVisible = true;
        this.lastPunishedAt = lastPunishedTime;
    }

    protected Post(Long postId, String content, PostType type, AuthorId authorId, UUID mediaId) {
        this.postId = postId;
        this.content = content;
        this.type = type;
        this.mediaId = mediaId;
        this.likeCount = 0;
        this.commentCount = 0;
        this.createdAt = OffsetDateTime.now();
        this.authorId = authorId;
        this.isVisible = true;
        this.score = 100;
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
        this.lastPunishedAt = OffsetDateTime.now();
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
