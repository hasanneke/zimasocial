package com.zimaberlin.zimasocial.entity.report;

import com.zimaberlin.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zimaberlin.zimasocial.context.contentmoderation.report.reports.PostReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "REPORT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction(value = "IS_DELETED IS FALSE")
@Getter
public class ReportEntity  {
    @EmbeddedId
    private ReportId id;

    @Column(name = "reported_post_owner_id")
    private Long reportedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_reason", nullable = false)
    private ReportReason reportReason;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public void markAsDeleted() {
        this.isDeleted = true;
    }
    public static ReportEntity buildPostReport(PostReport report) {
        return ReportEntity.builder()
                .id(new ReportId(report.postId(), report.reporterAuthorId().getValue(), ResourceType.post))
                .reportReason(report.reason())
                .description(report.description())
                .reportedUserId(report.reportedAuthorId().getValue())
                .build();
    }
    public static ReportEntity buildCommentReport(CommentReport report) {
        return ReportEntity.builder()
                .id(new ReportId(report.commentId(), report.reporterAuthorId().getValue(), ResourceType.comment))
                .reportReason(report.reason())
                .description(report.description())
                .reportedUserId(report.reportedAuthorId().getValue())
                .build();
    }
}
