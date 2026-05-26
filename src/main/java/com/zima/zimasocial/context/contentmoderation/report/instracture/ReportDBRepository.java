package com.zima.zimasocial.context.contentmoderation.report.instracture;

import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zima.zimasocial.context.contentmoderation.report.reports.PostReport;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.entity.report.ReportEntity;
import com.zima.zimasocial.entity.report.ReportId;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.repository.ReportJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDBRepository implements ReportRepository {
    private final ReportJpaRepository reportJpaRepository;

    @Autowired
    public ReportDBRepository(ReportJpaRepository reportJpaRepository) {
        this.reportJpaRepository = reportJpaRepository;
    }

    @Override
    public void save(PostReport report) {
        ReportEntity reportEntity =  ReportEntity.buildPostReport(report);
        reportJpaRepository.save(reportEntity);
    }

    @Override
    public void save(CommentReport report) {
        ReportEntity reportEntity =  ReportEntity.buildCommentReport(report);
        reportJpaRepository.save(reportEntity);
    }

    @Override
    public boolean checkReportExists(Long resourceId, AuthorId reporterId, ResourceType resourceType) {
        return reportJpaRepository.findById(ReportId.builder()
                .reporterId(reporterId.getValue())
                .resourceId(resourceId)
                .resourceType(resourceType)
                .build()).isPresent();
    }
}
