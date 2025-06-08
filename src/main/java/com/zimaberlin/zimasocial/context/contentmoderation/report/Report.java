package com.zimaberlin.zimasocial.context.contentmoderation.report;

import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
public class Report {
    private Long resourceId;
    private Long reporterAuthorId;
    private Long reportedAuthorId;
    private ResourceType resourceType;
    private ReportReason reason;
    private String description;

    public Report(Long resourceId, Long reporterAuthorId, Long reportedAuthorId, ResourceType resourceType, ReportReason reason, String description) {
        Assert.notNull(resourceId, "Resource Id cannot be null");
        Assert.notNull(reporterAuthorId, "Reported Author Id cannot be null");
        Assert.notNull(resourceType, "Resource Type cannot be null");
        Assert.notNull(reason, "Report Reason cannot be null");
        this.resourceId = resourceId;
        this.reporterAuthorId = reporterAuthorId;
        this.reportedAuthorId = reportedAuthorId;
        this.resourceType = resourceType;
        this.reason = reason;
        this.description = description;
    }

    public Report(Long resourceId, Long reporterAuthorId, ResourceType resourceType, ReportReason reason){
        Assert.notNull(resourceId, "Resource Id cannot be null");
        Assert.notNull(reporterAuthorId, "Reported Author Id cannot be null");
        Assert.notNull(resourceType, "Resource Type cannot be null");
        Assert.notNull(reason, "Report Reason cannot be null");

        this.resourceId = resourceId;
        this.reporterAuthorId = reporterAuthorId;
        this.resourceType = resourceType;
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(resourceId, report.resourceId) && Objects.equals(reporterAuthorId, report.reporterAuthorId) && resourceType == report.resourceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, reporterAuthorId, resourceType);
    }
}
