package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.comment.CommentLikedEvent;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.entity.CommentEntity;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.shared.StaticEventPublisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Embedded
    private AuthorId authorId;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne
    private Author author;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "reply_count")
    private int replyCount = 0;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private CommentEntity parent;

    @Column(name = "media_id")
    private UUID mediaId;

    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment reply(AuthorId replierAuthorId, String comment, UUID mediaId){
        replyCount += 1;
        return Comment.builder()
                .postId(postId)
                .authorId(replierAuthorId)
                .content(comment)
                .mediaId(mediaId)
                .parentId(id)
                .build();
    }

    public void removeReply() {
        replyCount -= 1;
    }

    public Like like(AuthorId likerId) {
        likeCount += 1;
        StaticEventPublisher.publishEvent(new CommentLikedEvent(this.postId, this.getId(), new AuthorDomainId(authorId.getValue()), new AuthorDomainId(likerId.getValue())));
        return Like.builder()
                .postId(postId)
                .commentId(id)
                .authorId(likerId)
                .type(LikeType.comment)
                .build();
    }

    public void unlike() {
        likeCount -= 1;
    }
}
