package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

public record PostLikedEvent(Long postId, AuthorDomainId postOwnerId, AuthorDomainId actorId) { }
