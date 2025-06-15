package com.zimaberlin.zimasocial.context.contentmoderation.report;

import com.zimaberlin.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zimaberlin.zimasocial.context.contentmoderation.report.content.PostContent;

import java.util.Optional;

public interface ContentRepository {
    Optional<PostContent> getPost(Long id);
    Optional<CommentContent> getComment(Long id);
}
