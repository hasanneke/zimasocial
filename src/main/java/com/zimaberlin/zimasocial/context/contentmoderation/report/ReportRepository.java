package com.zimaberlin.zimasocial.context.contentmoderation.report;

import com.zimaberlin.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zimaberlin.zimasocial.context.contentmoderation.report.reports.PostReport;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.entity.report.ResourceType;

import java.util.Optional;

public interface ReportRepository {
    void save(PostReport report);
    void save(CommentReport report);
    boolean checkReportExists(Long resourceId, AuthorId reporterId, ResourceType resourceType);
}
