package com.zima.zimasocial.context.social2.event;

import com.zima.zimasocial.context.social2.domain.entity.PostContent;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;

public record PostSharedEvent(Long postId, AuthorId postOwnerId, PostContent postContent) { }
