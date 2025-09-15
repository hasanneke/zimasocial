package com.zimaberlin.zimasocial.context.social.api.author;

import lombok.Getter;

@Getter
public class FollowRequestsInfo {
    private Integer followRequestCount;
    private AuthorView lastFollowRequester;

    public FollowRequestsInfo(Integer followRequestCount, AuthorView lastFollowRequester) {
        this.followRequestCount = followRequestCount;
        this.lastFollowRequester = lastFollowRequester;
    }

    public FollowRequestsInfo(Integer followRequestCount) {
        this.followRequestCount = followRequestCount;
    }
}
