package com.zimaberlin.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zimaberlin.zimasocial.entity.media.MediaJpa;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.context.social.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "post")
@SQLRestriction(value = "IS_DELETED IS false AND IS_VISIBLE IS true")
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<TodaysPost> todaysPosts =  new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private MediaJpa media;

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
        this.content = post.getContent();
        this.type = post.getType();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.userId = post.getAuthorId().getId();
        this.isVisible = post.getIsVisible();
    }
}

