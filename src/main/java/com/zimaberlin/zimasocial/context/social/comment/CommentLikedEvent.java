package com.zimaberlin.zimasocial.context.social.comment;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record CommentLikedEvent(Long postId, Long commentId, AuthorId commentOwnerId, AuthorId likerAuthorId) {
}
