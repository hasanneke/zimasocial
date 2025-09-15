package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class FollowRequestNotFoundException extends DataNotFoundException {
    public FollowRequestNotFoundException() {
        super("follow_request_not_found", "Follow request not found");
    }

    public FollowRequestNotFoundException(String message) {
        super(message);
    }
}
