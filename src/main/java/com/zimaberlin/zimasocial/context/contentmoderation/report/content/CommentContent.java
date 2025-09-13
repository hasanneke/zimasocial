package com.zimaberlin.zimasocial.context.contentmoderation.report.content;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record CommentContent(Long commentId, Long postId, Long parentCommentId, AuthorId authorId) {
}
