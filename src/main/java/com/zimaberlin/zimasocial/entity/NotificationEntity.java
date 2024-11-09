package com.zimaberlin.zimasocial.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Size(max = 64)
    @Column(name = "type", length = 64, nullable = false)
    private String type;

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
}
