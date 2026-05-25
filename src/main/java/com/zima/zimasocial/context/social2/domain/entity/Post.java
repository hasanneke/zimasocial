package com.zima.zimasocial.context.social2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.MediaId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import com.zima.zimasocial.entity.LikeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
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


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();


    public Post(PostId id,
                PostContent content,
                AuthorId authorId,
                MediaId mediaId) {
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

    public Comment comment(AuthorId commenterId,
                           String content,
                           UUID mediaId,
                           boolean hasNoPreviousComments) {
        if(hasNoPreviousComments){
            stats.updateScoreBy(5);
        }
        stats.incrementComment();
        return Comment
                .builder()
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
        stats.decrementComment();
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
