package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.post.value.PostId;

public record PostLikedEvent(PostId postId, AuthorId postOwnerId, AuthorId actorId) { }
