package com.zimaberlin.zimasocial.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.exception.UnauthorizedException;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import jakarta.persistence.*;
import lombok.*;
import org.apache.coyote.BadRequestException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "post")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<LikeEntity> likes =  new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setWithPayloadAndUser(UserEntity user, PostPayload payload){
        PostType type = payload.getType() != null ? payload.getType() : this.type;
        setUser(user);
        setContent(payload.getContent());
        setType(type);
        setUrl(payload.getUrl());
    }
    
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

}

