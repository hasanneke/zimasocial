package com.zimaberlin.zimasocial.context.contentmoderation.report;

import com.zimaberlin.zimasocial.entity.report.ResourceType;

import java.util.Optional;

public interface ReportRepository {
    void save(Report report);
    boolean checkReportExists(Long resourceId, Long reporterId, ResourceType resourceType);
}
