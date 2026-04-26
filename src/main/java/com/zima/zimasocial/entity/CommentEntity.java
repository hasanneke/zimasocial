package com.zima.zimasocial.entity;

import com.zima.zimasocial.context.social.comment.Comment;
import com.zima.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @Column(name = "media_id")
    private UUID mediaId;

    public void mergeDomain(Comment comment){
        this.likeCount = comment.getLikeCount();
        this.replyCount = comment.getReplyCount();
        this.content = comment.getContent();
        this.mediaId = comment.getMediaId();
    }
}
