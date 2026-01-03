package com.zimaberlin.zimasocial.context.social.post.value;

import com.zimaberlin.zimasocial.entity.MediaType;
import org.springframework.util.Assert;

public record PostContent (String content, MediaType type, MediaId mediaId) {
    public PostContent {
        Assert.notNull(type, "PostContent.type cannot be null");
        Assert.isTrue(!(type != MediaType.any && mediaId == null), "PostContent.mediaId cannot be null while PostContent.type is %s".formatted(type));
    }
}
