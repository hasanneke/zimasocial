package com.zimaberlin.zimasocial.entity;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<LikeEntity> likes = new HashSet<>();

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "reply_count")
    private int replyCount = 0;

    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentEntity parent;

    public void incrementLikeCount(){
        likeCount = likeCount + 1;
    }
    public void decrementLikeCount(){
        likeCount = likeCount - 1;
    }
    public void incrementReplyCount(){
        replyCount = replyCount + 1;
    }
    public void decrementReplyCount(){
        replyCount = replyCount - 1;
    }
}
