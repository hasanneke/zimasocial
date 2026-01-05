package com.zima.zimasocial.context.social.comment;

import com.zima.zimasocial.context.social.author.value.AuthorId;

public record CommentLikedEvent(Long postId, Long commentId, AuthorId commentOwnerId, AuthorId likerAuthorId) {
}
