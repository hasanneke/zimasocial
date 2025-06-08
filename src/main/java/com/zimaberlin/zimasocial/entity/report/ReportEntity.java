package com.zimaberlin.zimasocial.entity.report;

import com.zimaberlin.zimasocial.context.contentmoderation.report.Report;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
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

    public static ReportEntity buildReport(Report report){
        switch (report.getResourceType()){
            case ResourceType.post -> {
                return buildPostReport(report);
            }
            case ResourceType.comment -> {
                return buildCommentReport(report);
            }
            case ResourceType.profile -> {
                return buildProfileReport(report);
            }
            default ->{
                return null;
            }
        }
    }

    private static ReportEntity buildPostReport(Report report) {
        return ReportEntity.builder()
                .id(new ReportId(report.getResourceId(), report.getReporterAuthorId(), ResourceType.post))
                .reportReason(report.getReason())
                .description(report.getDescription())
                .reportedUserId(report.getReportedAuthorId())
                .build();
    }
    private static ReportEntity buildCommentReport(Report report) {
        return ReportEntity.builder()
                .id(new ReportId(report.getResourceId(), report.getReporterAuthorId(), ResourceType.comment))
                .reportReason(report.getReason())
                .description(report.getDescription())
                .reportedUserId(report.getReportedAuthorId())
                .build();
    }
    private static ReportEntity buildProfileReport(Report report) {
        return ReportEntity.builder()
                .id(new ReportId(report.getResourceId(), report.getReporterAuthorId(), ResourceType.profile))
                .reportReason(report.getReason())
                .description(report.getDescription())
                .reportedUserId(report.getReportedAuthorId())
                .build();
    }
}
