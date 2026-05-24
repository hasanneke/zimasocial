package com.zima.zimasocial.context.social.post.event;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

public record PostCommentedEvent(Long postId, Long commentId, AuthorDomainId actorId, AuthorDomainId commentOwnerId) {
}
