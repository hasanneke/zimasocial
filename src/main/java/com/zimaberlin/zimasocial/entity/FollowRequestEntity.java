package com.zimaberlin.zimasocial.entity;

import com.zimaberlin.zimasocial.context.social.authorrelation.entity.FollowRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "follow_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FollowRequestEntity {
    @Id
    private UUID id;
    private Long followerId;
    private Long followedId;
    @Column(name = "accepted")
    private Boolean isAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FollowRequestEntity(FollowRequest followRequest) {
        this.id = followRequest.getId();
        this.followerId = followRequest.getFollowerAuthorId().getValue();
        this.followedId = followRequest.getFollowedAuthorId().getValue();
        this.isAccepted = followRequest.getIsAccepted();
        this.createdAt = followRequest.getCreatedAt();
        this.updatedAt = followRequest.getUpdatedAt();
    }
}
