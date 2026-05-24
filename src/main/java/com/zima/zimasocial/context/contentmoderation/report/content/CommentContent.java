package com.zima.zimasocial.context.contentmoderation.report.content;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

public record CommentContent(Long commentId, Long postId, Long parentCommentId, AuthorDomainId authorId) {
}
