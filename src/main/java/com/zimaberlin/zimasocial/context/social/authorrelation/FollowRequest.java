package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class FollowRequest {
    private final UUID id;
    private final AuthorId followerAuthorId;
    private final AuthorId followedAuthorId;
    private Boolean isAccepted;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FollowRequest(UUID id, AuthorId followerAuthorId, AuthorId followedAuthorId, Boolean isAccepted, LocalDateTime createdAt) {
        this.id = id;
        this.followerAuthorId = followerAuthorId;
        this.followedAuthorId = followedAuthorId;
        this.isAccepted = isAccepted;
        this.createdAt = createdAt;
    }
    public FollowRequest(UUID id, AuthorId followerAuthorId, AuthorId followedAuthorId, Boolean isAccepted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.followerAuthorId = followerAuthorId;
        this.followedAuthorId = followedAuthorId;
        this.isAccepted = isAccepted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public void accept() {
        if(updatedAt != null) throw new FollowRequestAlreadyProcessed();
        this.isAccepted = true;
        this.updatedAt = LocalDateTime.now();
        StaticEventPublisher.publishEvent(new AuthorFollowRequestAcceptedEvent(followerAuthorId, followedAuthorId));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowRequest that = (FollowRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
