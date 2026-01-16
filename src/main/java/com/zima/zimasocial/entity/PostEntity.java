package com.zima.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.value.MediaId;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.entity.todayspost.TodaysPost;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.views.post.PostDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
public class PostEntity {
    @Id
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    @JsonIgnore
    private UserEntity user;

    @Column(name = "is_visible")
    private Boolean isVisible = true;

    @Column(name = "user_id",insertable = false, updatable = false)
    private Long userId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "last_punished_at")
    private LocalDateTime lastPunishedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<TodaysPost> todaysPosts =  new HashSet<>();

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "media_id")
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

    public void merge(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent().content();
        this.type = post.getContent().type();
        this.mediaId = post.getContent().mediaId() != null ? post.getContent().mediaId().value() : null;
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
                new PostContent(content, type, mediaId != null ? new MediaId(mediaId) : null),
                likeCount,
                commentCount,
                score,
                isVisible,
                createdAt,
                updatedAt,
                lastPunishedAt);
    }
}

