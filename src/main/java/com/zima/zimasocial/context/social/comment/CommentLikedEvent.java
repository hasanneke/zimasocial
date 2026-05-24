package com.zima.zimasocial.context.social.comment;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

public record CommentLikedEvent(Long postId, Long commentId, AuthorDomainId commentOwnerId, AuthorDomainId likerAuthorId) {
}
