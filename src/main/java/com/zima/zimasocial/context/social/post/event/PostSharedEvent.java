package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.author.value.AuthorId;

public record PostSharedEvent(Long postId, AuthorId postOwnerId, PostContent postContent) { }
