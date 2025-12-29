package com.zimaberlin.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.context.social.post.value.MediaId;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import com.zimaberlin.zimasocial.context.social.post.value.PostContent;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "post")
@SQLRestriction(value = "IS_DELETED IS false")
public class PostEntity {
    @Id
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @Column(name = "is_visible")
    private Boolean isVisible = true;

    @Column(name = "user_id",insertable = false, updatable = false)
    private Long userId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "last_punished_at")
    private OffsetDateTime lastPunishedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<TodaysPost> todaysPosts =  new HashSet<>();

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "media_item_id")
    private UUID mediaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void merge(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent().content();
        this.type = post.getContent().type();
        this.mediaId = post.getContent().mediaId().value();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.userId = post.getAuthorId().getValue();
        this.isVisible = post.getIsVisible();
        this.score = post.getScore();
        this.lastPunishedAt = post.getLastPunishedAt();
        this.updatedAt = post.getUpdatedAt();

    }

    public Post rehydrate() {
        return Post.reconstitute(
                id,
                new AuthorId(userId),
                new PostContent(content, type, new MediaId(mediaId)),
                likeCount,
                commentCount,
                score,
                isVisible,
                createdAt,
                updatedAt,
                lastPunishedAt);
    }
}

