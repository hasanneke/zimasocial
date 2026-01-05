package com.zima.zimasocial.context.contentmoderation.report;

import com.zima.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zima.zimasocial.context.contentmoderation.report.reports.PostReport;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.entity.report.ResourceType;

public interface ReportRepository {
    void save(PostReport report);
    void save(CommentReport report);
    boolean checkReportExists(Long resourceId, AuthorId reporterId, ResourceType resourceType);
}
