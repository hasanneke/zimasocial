package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "follow_request")
@Getter
public class FollowRequest {
    protected FollowRequest() {}

    public FollowRequest(UUID id, AuthorId followerId, AuthorId followedId) {
        this.id = id;
        this.followerId = followerId;
        this.followedId = followedId;
    }

    @Id
    private UUID id;
    @AttributeOverride(
            name = "value",
            column = @Column(name = "follower_id", updatable = false)
    )
    @Embedded
    private AuthorId followerId;

    @AttributeOverride(
            name = "value",
            column = @Column(name = "followed_id", updatable = false)
    )
    @Embedded
    private AuthorId followedId;

    @Column(name = "accepted", nullable = false)
    private Boolean isAccepted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    public void accept() {
        this.isAccepted = true;
        this.updatedAt = LocalDateTime.now();
    }
}
