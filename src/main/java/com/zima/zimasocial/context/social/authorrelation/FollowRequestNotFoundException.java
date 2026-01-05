package com.zima.zimasocial.context.social.authorrelation;

import com.zima.zimasocial.exception.DataNotFoundException;

public class FollowRequestNotFoundException extends DataNotFoundException {
    public FollowRequestNotFoundException() {
        super("follow_request_not_found", "Follow request not found");
    }

    public FollowRequestNotFoundException(String message) {
        super(message);
    }
}
