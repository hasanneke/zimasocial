package com.zima.zimasocial.context.social.post.entity;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.media.value.MediaId;
import com.zima.zimasocial.context.social.media.value.MediaType;
import com.zima.zimasocial.context.social.post.api.views.PostDTO;
import com.zima.zimasocial.context.social.post.value.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@SqlResultSetMapping(name = "post_dto_mapping", classes = {
        @ConstructorResult(
                targetClass = PostDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "content", type = String.class),
                        @ColumnResult(name = "type", type = String.class),
                        @ColumnResult(name = "likeCount", type = Long.class),
                        @ColumnResult(name = "commentCount", type = Long.class),
                        @ColumnResult(name = "isLiked", type = Boolean.class),
                        @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                        @ColumnResult(name = "updatedAt", type = LocalDateTime.class),
                        @ColumnResult(name = "isReported", type = Boolean.class),
                        @ColumnResult(name = "mediaId", type = UUID.class),
                        @ColumnResult(name = "slug", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "familyName", type = String.class),
                        @ColumnResult(name = "avatarFileName", type = String.class),
                        @ColumnResult(name = "bio", type = String.class),
                        @ColumnResult(name = "followerCount", type = Long.class),
                        @ColumnResult(name = "followingCount", type = Long.class),
                        @ColumnResult(name = "isFollowed", type = Boolean.class),
                        @ColumnResult(name = "isFollowingMe", type = Boolean.class),
                        @ColumnResult(name = "isFollowRequestSent", type = Boolean.class),
                        @ColumnResult(name = "isFollowRequestReceived", type = Boolean.class),
                        @ColumnResult(name = "score", type = Long.class),
                }
        )
})
public class Post {
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private PostId id;

    @Embedded
    private AuthorId authorId;

    @Embedded
    private PostContent content;

    @Embedded
    private MediaId mediaId;

    @Embedded
    private PostStats stats;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = true;

    @Column(name = "last_punished_at")
    private LocalDateTime lastPunishedAt;

    protected Post() {}

    public Post(PostId id,
                PostContent content,
                AuthorId authorId,
                MediaId mediaId) {
        Assert.notNull(id, "Id cannot be null");
        Assert.notNull(content, "content cannot be null");
        Assert.notNull(authorId, "AuthorId cannot be null");
        if(content.getType() != MediaType.any){
            Assert.notNull(mediaId, "MediaId cannot be null if type is not MediaType.any");
        }
        this.id = id;
        this.content = content;
        this.mediaId = mediaId;
        this.authorId = authorId;
        this.createdAt = LocalDateTime.now();
        this.stats = PostStats.builder()
                .likeCount(0)
                .commentCount(0)
                .score(100)
                .build();
    }

    public static Post create(
            PostId id,
            PostContent content,
            AuthorId authorId,
            MediaId mediaId
    ) {
        return new Post(id, content, authorId, mediaId);
    }

    public Like like(AuthorId likerId) {
        stats.incrementLike();
        if(!likerId.equals(authorId)){
            stats.updateScoreBy(2);
        }
        return Like.builder()
                .postId(id)
                .type(LikeType.post)
                .authorId(likerId)
                .build();
    }

    public void unlike(AuthorId likerId) {
        stats.decrementLike();
        if(!authorId.equals(likerId)){
            stats.updateScoreBy(-2);
        }
    }

    public Comment comment(
            CommentId commentId,
            AuthorId commenterId,
                           String content,
                           MediaId mediaId,
                           boolean hasNoPreviousComments) {
        if(hasNoPreviousComments){
            stats.updateScoreBy(5);
        }
        stats.incrementComment();
        return Comment
                .builder()
                .id(commentId)
                .authorId(commenterId)
                .postId(id)
                .content(content)
                .mediaId(mediaId)
                .build();
    }

    public void decreaseCommentScore() {
        stats.updateScoreBy(-5);
    }

    public void removeComment(AuthorId commenterId, boolean hasNoPreviousComments) {
        stats.decrementCommentCount();
        if(!commenterId.equals(authorId) && hasNoPreviousComments){
            stats.updateScoreBy(-5);
        }
    }

    public void makeInvisible() {
        this.isVisible = false;
    }
    public void makeVisible() {
        this.isVisible = true;
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

    private boolean isPunishable() {
        long passedHoursFromCreation = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        return passedHoursFromCreation < 72;
    }

    private void punish() {
        stats.updateScoreBy(-(int) (stats.getScore() * 0.04));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return id != null && id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
