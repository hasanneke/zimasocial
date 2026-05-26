package com.zima.zimasocial.context.contentmoderation.report.content;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;

public record PostContent(Long postId, AuthorId authorId) {
}
