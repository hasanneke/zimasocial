package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.social.authorrelation.entity.FollowRequest;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class FollowRequestDTO {
    private UUID id;
    private AuthorView followerAuthor;
    private AuthorView followedAuthor;
    private Boolean isAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FollowRequestDTO(FollowRequest followRequest, AuthorView followerAuthor, AuthorView followedAuthor) {
        this.id = followRequest.getId();
        this.followerAuthor = followerAuthor;
        this.followedAuthor = followedAuthor;
        this.isAccepted = followRequest.getIsAccepted();
        this.createdAt = followRequest.getCreatedAt();
        this.updatedAt = followRequest.getUpdatedAt();
    }
}
