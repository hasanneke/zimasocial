package com.zima.zimasocial.context.social.post.entity;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.media.value.MediaId;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.context.social.post.event.CommentLikedEvent;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.shared.StaticEventPublisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Builder
@Getter
@AllArgsConstructor
public class Comment {
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private CommentId id;

    @Embedded
    private PostId postId;

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

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "parent_id", updatable = false)
    )
    private CommentId parentId;

    @ManyToOne
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Comment parent;

    @Embedded
    private MediaId mediaId;

    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Comment() {}

    public Comment(CommentId id, AuthorId authorId, PostId postId, String content, MediaId mediaId) {
        Assert.notNull(id, "Id cannot be null");
        Assert.notNull(id, "AuthorId cannot be null");
        Assert.notNull(id, "PostId cannot be null");
        Assert.notNull(id, "Content cannot be null");
        this.id = id;
        this.authorId = authorId;
        this.postId = postId;
        this.content = content;
        this.mediaId = mediaId;
        this.createdAt = LocalDateTime.now();
    }

    public Comment reply(AuthorId replierAuthorId, String comment, MediaId mediaId){
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
        StaticEventPublisher.publishEvent(new CommentLikedEvent(this.getPostId(), this.getId(), authorId, likerId));
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
