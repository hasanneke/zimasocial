package com.zima.zimasocial.context.contentmoderation.report.reports;

import com.zima.zimasocial.context.contentmoderation.ReportReason;

public class UserReport {
    private Long userId;
    private ReportReason reason;
    private Long reporterAuthorId;
    private String description;
}
