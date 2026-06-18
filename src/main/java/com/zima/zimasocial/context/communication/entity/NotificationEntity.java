package com.zima.zimasocial.context.communication.entity;

import com.zima.zimasocial.context.communication.domain.entity.*;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.media.value.MediaType;
import com.zima.zimasocial.context.communication.value.NotificationType;
import com.zima.zimasocial.context.communication.value.TargetCollection;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

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
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "receiver_user_id")
    private Long receiverUserId;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", insertable = false, updatable = false)
    private Author receiverUser;

    @Column(name = "sender_user_id")
    private Long actorId;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", insertable = false, updatable = false)
    private Author actor;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "target_collection")
    private TargetCollection targetCollection;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "post_id")
    private Long postId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type")
    private MediaType postType;

    @Column(name = "chat_id")
    private UUID chatId;

    @Column(name = "pushed")
    private Boolean isPushed = false;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default()
    private Boolean isDeleted = false;

    public static NotificationEntity buildPostSharedNotification(PostSharedNotification postSharedNotification) {
        return NotificationEntity.builder()
                .id(postSharedNotification.getId())
                .postId(postSharedNotification.getPostId())
                .type(NotificationType.POST_SHARED)
                .postType(postSharedNotification.getType())
                .content(postSharedNotification.getContent())
                .targetCollection(TargetCollection.post)
                .actorId(postSharedNotification.getActorId().getValue())
                .isPushed(postSharedNotification.isPushed())
                .build();
        }

    public void markAsDeleted(){
        this.isDeleted = true;
    }

    public static NotificationEntity buildPostLikedNotification(PostLikedNotification postLikedNotification, Recipient receiver, Recipient actor) {
        return NotificationEntity.builder()
                .id(postLikedNotification.getId())
                .postId(postLikedNotification.getPostId())
                .type(NotificationType.POST_LIKED)
                .content(postLikedNotification.getMessage())
                .targetId(postLikedNotification.getPostId())
                .targetCollection(TargetCollection.post)
                .receiverUserId(receiver.getId().getValue())
                .isPushed(postLikedNotification.isPushed())
                .actorId(actor.getId().getValue())
                .build();
    }
    public static NotificationEntity buildPostCommentedNotification(PostCommentedNotification postCommentedNotification, Recipient receiver, Recipient actor) {
        return NotificationEntity.builder()
                .id(postCommentedNotification.getId())
                .postId(postCommentedNotification.getPostId().getValue())
                .type(NotificationType.POST_COMMENTED)
                .content(postCommentedNotification.getMessage())
                .targetId(postCommentedNotification.getPostId().getValue())
                .targetCollection(TargetCollection.post)
                .receiverUserId(receiver.getId().getValue())
                .isPushed(postCommentedNotification.isPushed())
                .actorId(actor.getId().getValue())
                .build();
    }
    public static NotificationEntity buildCommentLikedNotification(CommentLikedNotification commentLikedNotification, Recipient receiver, Recipient actor) {
        return NotificationEntity.builder()
                .id(commentLikedNotification.getId())
                .postId(commentLikedNotification.getPostId().getValue())
                .type(NotificationType.COMMENT_LIKED)
                .content(commentLikedNotification.getMessage())
                .targetId(commentLikedNotification.getCommentId().getValue())
                .targetCollection(TargetCollection.comment)
                .receiverUserId(receiver.getId().getValue())
                .actorId(actor.getId().getValue())
                .isPushed(commentLikedNotification.isPushed())
                .build();
    }

    public static NotificationEntity buildCommentRepliedNotification(CommentRepliedNotification commentRepliedNotification, Recipient recipient, Recipient actor) {
        return NotificationEntity.builder()
                .id(commentRepliedNotification.getId())
                .postId(commentRepliedNotification.getPostId().getValue())
                .type(NotificationType.COMMENT_REPLIED)
                .content(commentRepliedNotification.getMessage())
                .targetId(commentRepliedNotification.getReplyId().getValue())
                .targetCollection(TargetCollection.comment)
                .receiverUserId(recipient.getId().getValue())
                .actorId(actor.getId().getValue())
                .isPushed(commentRepliedNotification.isPushed())
                .build();
    }
    public static NotificationEntity buildAuthorFollowedNotification(AuthorFollowedNotification authorFollowedNotification) {
        return NotificationEntity.builder()
                .id(authorFollowedNotification.getId())
                .type(NotificationType.USER_FOLLOWED_YOU)
                .content(authorFollowedNotification.getMessage())
                .targetId(authorFollowedNotification.getRecipientId().getValue())
                .targetCollection(TargetCollection.profile)
                .receiverUserId(authorFollowedNotification.getRecipientId().getValue())
                .actorId(authorFollowedNotification.getActorId().getValue())
                .isPushed(authorFollowedNotification.isPushed())
                .build();
    }

    public static NotificationEntity buildAuthorFollowRequestSentNotification(AuthorFollowRequestSentNotification authorFollowRequestSentNotification) {
        return NotificationEntity.builder()
                .id(authorFollowRequestSentNotification.getId())
                .type(NotificationType.USER_SENT_FOLLOW_REQUEST)
                .content(authorFollowRequestSentNotification.getMessage())
                .targetId(authorFollowRequestSentNotification.getActorId().getValue())
                .targetCollection(TargetCollection.profile)
                .receiverUserId(authorFollowRequestSentNotification.getRecipientId().getValue())
                .actorId(authorFollowRequestSentNotification.getActorId().getValue())
                .isPushed(authorFollowRequestSentNotification.isPushed())
                .build();
    }

    public static NotificationEntity buildAuthorFollowRequestAcceptedNotification(AuthorFollowRequestAcceptedNotification authorFollowRequestAcceptedNotification) {
        return NotificationEntity.builder()
                .id(authorFollowRequestAcceptedNotification.getId())
                .type(NotificationType.USER_FOLLOW_REQUEST_ACCEPTED)
                .content(authorFollowRequestAcceptedNotification.getMessage())
                .targetId(authorFollowRequestAcceptedNotification.getActorId().getValue())
                .targetCollection(TargetCollection.profile)
                .receiverUserId(authorFollowRequestAcceptedNotification.getRecipientId().getValue())
                .actorId(authorFollowRequestAcceptedNotification.getActorId().getValue())
                .isPushed(authorFollowRequestAcceptedNotification.isPushed())
                .build();
    }

    public static NotificationEntity buildNewMessageNotification(ChatMessageSentNotification chatMessageSentNotification) {
        return NotificationEntity.builder()
                .id(chatMessageSentNotification.getId())
                .type(NotificationType.NEW_MESSAGE)
                .content(chatMessageSentNotification.getMessage())
                .targetId(chatMessageSentNotification.getActorId().getValue())
                .targetCollection(TargetCollection.chat)
                .receiverUserId(chatMessageSentNotification.getRecipientId().getValue())
                .actorId(chatMessageSentNotification.getActorId().getValue())
                .isPushed(chatMessageSentNotification.isPushed())
                .chatId(chatMessageSentNotification.getChatRoomId().getValue())
                .build();
    }
    public void merge(Notification notification){
        this.isPushed = notification.isPushed();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationEntity that = (NotificationEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
