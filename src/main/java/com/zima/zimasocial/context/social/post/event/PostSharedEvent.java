package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.value.PostContent;

public record PostSharedEvent(Long postId, AuthorId postOwnerId, PostContent postContent) { }
