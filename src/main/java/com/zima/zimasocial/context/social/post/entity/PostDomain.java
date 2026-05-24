package com.zima.zimasocial.context.social.post.entity;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.comment.CommentDomain;
import com.zima.zimasocial.context.social.post.event.PostLikedEvent;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Getter
public class PostDomain {
    private Long postId;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AuthorDomainId authorId;
    private PostContent content;
    private Boolean isVisible;
    private Integer score;
    private LocalDateTime lastPunishedAt;
    private PostDomain(Long postId) {
        this.postId = Objects.requireNonNull(postId, "Post id cannot be null");
    }

    public static PostDomain create(
            Long postId,
            AuthorDomainId authorId,
            PostContent content
    ) {
        Assert.notNull(authorId, "Author is required");

        LocalDateTime now = LocalDateTime.now();

        PostDomain post = new PostDomain(postId);
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

    public static PostDomain reconstitute(
            Long postId,
            AuthorDomainId authorId,
            PostContent content,
            int likeCount,
            int commentCount,
            int score,
            boolean visible,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastPunishedAt
    ) {
        PostDomain post = new PostDomain(postId);
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

    public PostLike like(AuthorDomainId likerAuthorId) {
        likeCount += 1;
        if(!authorId.equals(likerAuthorId)){
            score += 2;
        }
        StaticEventPublisher.publishEvent(new PostLikedEvent(postId, authorId, likerAuthorId));
        return new PostLike(postId, likerAuthorId);
    }
    public CommentDomain comment(AuthorDomainId commenterAuthorId, String comment, UUID mediaId) {
        commentCount += 1;
        if(!authorId.equals(commenterAuthorId)){
            score += 5;
        }
        return new CommentDomain(postId, null, commenterAuthorId, comment, mediaId);
    }
    public void removeComment(AuthorDomainId commenterId) {
        commentCount = commentCount - 1;
        if(!authorId.equals(commenterId)){
            score -= 5;
        }
    }
    public void unliked(AuthorDomainId unlikerAuthorId) {
        likeCount = likeCount - 1;
        if(!authorId.equals(unlikerAuthorId)){
            score -= 2;
        }
    }

    public void punishScore() {
        if(!isPunishable()) return;
        long hoursPassedSinceLastPunishment = ChronoUnit.HOURS.between(lastPunishedAt == null ? createdAt : lastPunishedAt, LocalDateTime.now());
        if(hoursPassedSinceLastPunishment == 0) return;
        for (long i = 0; i < hoursPassedSinceLastPunishment; i++) {
            punish();
        }
        this.lastPunishedAt = LocalDateTime.now();
    }

    private void punish() {
        score -= (int) (score * 0.04);
    }

    private boolean isPunishable() {
        long passedHoursFromCreation = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        return passedHoursFromCreation < 72;
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
        PostDomain post = (PostDomain) o;
        return Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }
}
