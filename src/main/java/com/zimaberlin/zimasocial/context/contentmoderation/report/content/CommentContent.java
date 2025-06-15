package com.zimaberlin.zimasocial.context.contentmoderation.report.content;

import lombok.Getter;

public record CommentContent(Long commentId, Long postId, Long parentCommentId, Long authorId) {
}
