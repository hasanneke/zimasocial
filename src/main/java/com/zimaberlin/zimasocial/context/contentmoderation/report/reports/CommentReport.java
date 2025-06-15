package com.zimaberlin.zimasocial.context.contentmoderation.report.reports;

import com.zimaberlin.zimasocial.entity.report.ReportReason;
import org.springframework.util.Assert;

import java.util.Objects;

public record CommentReport(Long commentId, ReportReason reason, Long reporterAuthorId, Long reportedAuthorId,
                            String description) {
    public CommentReport {
        Assert.notNull(commentId, "Comment id cannot be null");
        Assert.notNull(reason, "Reason cannot be null");
        Assert.notNull(reporterAuthorId, "Reporter author id cannot be null");
        Assert.notNull(reportedAuthorId, "Reported author id cannot be null");

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentReport that = (CommentReport) o;
        return Objects.equals(commentId, that.commentId) && reason == that.reason && Objects.equals(reporterAuthorId, that.reporterAuthorId) && Objects.equals(reportedAuthorId, that.reportedAuthorId) && Objects.equals(description, that.description);
    }

}
