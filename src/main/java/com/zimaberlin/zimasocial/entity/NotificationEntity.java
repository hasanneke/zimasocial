package com.zimaberlin.zimasocial.entity;

import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class NotificationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "content", length = 512, nullable = false)
    @Size(max = 512, message = "Content cannot exceed 512 characters")
    private String content;

    @Size(max = 512, message = "Url cannot exceed 512 characters")
    @Column(name = "url", length = 512)
    private String url;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "receiver_user_id")
    private Long receiverUserId;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", insertable = false, updatable = false)
    private UserEntity receiverUser;

    @Column(name = "sender_user_id")
    private Long actorId;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", insertable = false, updatable = false)
    private UserEntity actor;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "target_collection")
    private TargetCollection targetCollection;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default()
    private Boolean isDeleted = false;
    public void markAsDeleted(){
        this.isDeleted = true;
    }

    public static NotificationEntity buildPostLikedNotification(PostLikedNotification postLikedNotification, UserEntity recipient, UserEntity actor) {
        return NotificationEntity.builder()
                .postId(postLikedNotification.getPostId())
                .type(NotificationType.POST_LIKED)
                .content(postLikedNotification.getMessage())
                .targetId(postLikedNotification.getPostId())
                .targetCollection(TargetCollection.post)
                .receiverUserId(recipient.getId())
                .actorId(actor.getId())
                .build();
    }
    public static NotificationEntity buildPostCommentedNotification(PostCommentedNotification postCommentedNotification, UserEntity recipient, UserEntity actor) {
        return NotificationEntity.builder()
                .postId(postCommentedNotification.getPostId())
                .type(NotificationType.POST_COMMENTED)
                .content(postCommentedNotification.getMessage())
                .targetId(postCommentedNotification.getPostId())
                .targetCollection(TargetCollection.post)
                .receiverUserId(recipient.getId())
                .actorId(actor.getId())
                .build();
    }
    public static NotificationEntity buildCommentLikedNotification(CommentLikedNotification commentLikedNotification, UserEntity recipient, UserEntity actor) {
        return NotificationEntity.builder()
                .postId(commentLikedNotification.getPostId())
                .type(NotificationType.COMMENT_LIKED)
                .content(commentLikedNotification.getMessage())
                .targetId(commentLikedNotification.getCommentId())
                .targetCollection(TargetCollection.comment)
                .receiverUserId(recipient.getId())
                .actorId(actor.getId())
                .build();
    }

    public static NotificationEntity buildCommentRepliedNotification(CommentRepliedNotification commentRepliedNotification, UserEntity recipient, UserEntity actor) {
        return NotificationEntity.builder()
                .postId(commentRepliedNotification.getPostId())
                .type(NotificationType.COMMENT_REPLIED)
                .content(commentRepliedNotification.getMessage())
                .targetId(commentRepliedNotification.getReplyId())
                .targetCollection(TargetCollection.comment)
                .receiverUserId(recipient.getId())
                .actorId(actor.getId())
                .build();
    }
    public static NotificationEntity buildAuthorFollowedNotification(AuthorFollowedNotification authorFollowedNotification) {
        return NotificationEntity.builder()
                .type(NotificationType.USER_FOLLOWED_YOU)
                .content(authorFollowedNotification.getMessage())
                .targetId(authorFollowedNotification.getRecipientId().getId())
                .targetCollection(TargetCollection.profile)
                .receiverUserId(authorFollowedNotification.getRecipientId().getId())
                .actorId(authorFollowedNotification.getActorId().getId())
                .build();
    }
}
