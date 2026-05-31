package com.zima.zimasocial.context.social2.exception;

import com.zima.zimasocial.exception.DataNotFoundException;

public class FollowRequestNotFoundException extends DataNotFoundException {
    public FollowRequestNotFoundException() {
        super("follow_request_not_found", "Follow request not found");
    }
}
