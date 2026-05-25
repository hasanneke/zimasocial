package com.zima.zimasocial.context.social2.api.views;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PostDTO {
    private final Long id;
    private final String content;
    private final String type;
    private final Long likeCount;
    private final Long commentCount;
    private final Boolean isLiked;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean isReported;
    private final UUID mediaId;
    private final String slug;
    private final String name;
    private final String familyName;
    private final String avatarFileName;
    private final String bio;
    private final Long followerCount;
    private final Long followingCount;
    private final Boolean isFollowed;
    private final Boolean isFollowingMe;
    private final Boolean isFollowRequestSent;
    private final Boolean isFollowRequestReceived;
    private final Long score;

    public PostDTO(Long id, String content, String type, Long likeCount, Long commentCount, Boolean isLiked, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isReported, UUID mediaId, String slug, String name, String familyName, String avatarFileName, String bio, Long followerCount, Long followingCount, Boolean isFollowed, Boolean isFollowingMe, Boolean isFollowRequestSent, Boolean isFollowRequestReceived, Long score) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isLiked = isLiked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isReported = isReported;
        this.mediaId = mediaId;
        this.slug = slug;
        this.name = name;
        this.familyName = familyName;
        this.avatarFileName = avatarFileName;
        this.bio = bio;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.isFollowed = isFollowed;
        this.isFollowingMe = isFollowingMe;
        this.isFollowRequestSent = isFollowRequestSent;
        this.isFollowRequestReceived = isFollowRequestReceived;
        this.score = score;
    }
}
