package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.post.value.PostContent;

public record PostSharedEvent(Long postId, AuthorDomainId postOwnerId, PostContent postContent) { }
