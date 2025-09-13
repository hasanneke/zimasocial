package com.zimaberlin.zimasocial.context.contentmoderation.report.content;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;

public record PostContent(Long postId, AuthorId authorId) {
}
