package com.zimaberlin.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.context.social.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.devtools.remote.server.Dispatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

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
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void incrementLikeCount(){
        likeCount = likeCount + 1;
    }
    public void decrementLikeCount(){
        likeCount = likeCount - 1;
    }
    public void incrementCommentCount(){
        commentCount = commentCount + 1;
    }
    public void decrementCommentCount(){
        commentCount = commentCount - 1;
    }

    public void liked() {
        likeCount = likeCount + 1;
    }
    public void commented() {
        commentCount = commentCount + 1;
    }
    public void commentRemoved() {
        commentCount = commentCount - 1;
    }
    public void unliked() {
        likeCount = likeCount - 1;
    }
    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void merge(Post post) {
        this.content = post.getContent();
        this.url = post.getUrl();
        this.type = post.getType();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
    }
}

