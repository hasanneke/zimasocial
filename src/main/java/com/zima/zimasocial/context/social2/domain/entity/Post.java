package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.entity.LikeType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
public class Post {
    @Id
    private Long id;

    @Column(name = "user_id",insertable = false, updatable = false)
    private Long userId;

    @Embedded
    private PostContent content;

    @Embedded
    private PostMedia media;

    @Embedded
    private PostStats stats;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_visible")
    private Boolean isVisible = true;


    public Like like(Long likerId) {
        stats.incrementLike();
        if(!likerId.equals(userId)){
            stats.updateScoreBy(2);
        }
        return Like.builder()
                .postId(id)
                .type(LikeType.post)
                .userId(likerId)
                .build();
    }

    public void unlike(Long likerId) {
        stats.decrementLike();
        if(!userId.equals(likerId)){
            stats.updateScoreBy(-2);
        }
    }

    public Comment comment(Long commenterId,
                           String content,
                           UUID mediaId,
                           boolean hasNoPreviousComments) {
        if(hasNoPreviousComments){
            stats.updateScoreBy(5);
        }
        stats.incrementComment();
        return Comment
                .builder()
                .userId(commenterId)
                .postId(id)
                .content(content)
                .mediaId(mediaId)
                .build();
    }

    public void decreaseCommentScore() {
        stats.updateScoreBy(-5);
    }

    public void removeComment(Long commenterId, boolean hasNoPreviousComments) {
        stats.decrementComment();
        if(!commenterId.equals(userId) && hasNoPreviousComments){
            stats.updateScoreBy(-5);
        }
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
