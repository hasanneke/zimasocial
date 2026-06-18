package com.zima.zimasocial.context.social.author.exception;

import com.zima.zimasocial.shared.exception.DataNotFoundException;

public class FollowRequestNotFoundException extends DataNotFoundException {
    public FollowRequestNotFoundException() {
        super("follow_request_not_found", "Follow request not found");
    }
}
