package com.zima.zimasocial.context.social.authorrelation;

import com.zima.zimasocial.exception.ConflictException;

public class FollowRequestAlreadyProcessed extends ConflictException {
    public FollowRequestAlreadyProcessed() {
        super("follow_request_already_processed", "Follow request is already processed");
    }

    public FollowRequestAlreadyProcessed(String message) {
        super(message);
    }
}
