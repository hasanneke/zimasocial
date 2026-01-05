package com.zima.zimasocial.context.social.post.value;

import org.springframework.util.Assert;

import java.util.UUID;

public record MediaId (UUID value) {
    public MediaId {
        Assert.notNull(value, "MediaId.value cannot be null");
    }
}
