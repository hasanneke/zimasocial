package com.zima.zimasocial.context.contentmoderation.report.reports;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.contentmoderation.ReportReason;
import org.springframework.util.Assert;

import java.util.Objects;

public record PostReport(Long postId, ReportReason reason, AuthorId reporterAuthorId, AuthorId reportedAuthorId,
                         String description) {
    public PostReport {
        Assert.notNull(postId, "Post id cannot be null");
        Assert.notNull(reason, "Reason cannot be null");
        Assert.notNull(reporterAuthorId, "Reporter author id cannot be null");
        Assert.notNull(reportedAuthorId, "Reported author id cannot be null");

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostReport that = (PostReport) o;
        return Objects.equals(postId, that.postId) && reason == that.reason && Objects.equals(reporterAuthorId, that.reporterAuthorId) && Objects.equals(reportedAuthorId, that.reportedAuthorId) && Objects.equals(description, that.description);
    }

}
