package com.zima.zimasocial.context.social2.event;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.domain.value.PostId;

public record PostLikedEvent(PostId postId, AuthorId postOwnerId, AuthorId actorId) { }
