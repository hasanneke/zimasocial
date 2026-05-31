package com.zima.zimasocial.context.social.author.api.view;


import com.zima.zimasocial.context.social.author.entity.FollowRequest;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class FollowRequestView {
    private UUID id;
    private AuthorView followerAuthor;
    private AuthorView followedAuthor;
    private Boolean isAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FollowRequestView(FollowRequest followRequest, AuthorView followerAuthor, AuthorView followedAuthor) {
        this.id = followRequest.getId();
        this.followerAuthor = followerAuthor;
        this.followedAuthor = followedAuthor;
        this.isAccepted = followRequest.getIsAccepted();
        this.createdAt = followRequest.getCreatedAt();
        this.updatedAt = followRequest.getUpdatedAt();
    }
}
