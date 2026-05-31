package com.zima.zimasocial.context.social2.domain.event;

import com.zima.zimasocial.context.social2.domain.value.PostContent;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;

public record PostSharedEvent(Long postId, AuthorId postOwnerId, PostContent postContent) { }
