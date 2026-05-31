package com.zima.zimasocial.context.social2.api.views;

import lombok.Getter;

@Getter
public class LikeView {
    private final String slug;
    private final String fullName;
    private final String avatarUrl;
    private final boolean isFollowed;
    private final boolean isFollowingMe;
    private final boolean isFollowRequestSent;
    private final boolean isFollowRequestReceived;
    private final Boolean isPrivate;

    public LikeView(String slug, String fullName, String avatarUrl, boolean isFollowed, boolean isFollowingMe, boolean isFollowRequestSent, boolean isFollowRequestReceived, Boolean isPrivate) {
        this.slug = slug;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.isFollowed = isFollowed;
        this.isFollowingMe = isFollowingMe;
        this.isFollowRequestSent = isFollowRequestSent;
        this.isFollowRequestReceived = isFollowRequestReceived;
        this.isPrivate = isPrivate;
    }
}
