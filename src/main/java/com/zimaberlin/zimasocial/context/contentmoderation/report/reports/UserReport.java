package com.zimaberlin.zimasocial.context.contentmoderation.report.reports;

import com.zimaberlin.zimasocial.entity.report.ReportReason;

public class UserReport {
    private Long userId;
    private ReportReason reason;
    private Long reporterAuthorId;
    private String description;
}
