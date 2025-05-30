package com.zimaberlin.zimasocial.entity;

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

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private UserEntity receiverUser;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private UserEntity senderUser;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "target_collection")
    private TargetCollection targetCollection;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "post_id")
    private Long postId;
}
