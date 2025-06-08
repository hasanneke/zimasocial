package com.zimaberlin.zimasocial.context.contentmoderation.report.instracture;

import com.zimaberlin.zimasocial.context.contentmoderation.report.Report;
import com.zimaberlin.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zimaberlin.zimasocial.context.contentmoderation.report.UnknownResourceTypeException;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.repository.ReportJpaRepository;
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
    public void save(Report report) {
        ReportEntity reportEntity =  ReportEntity.buildReport(report);
        if(reportEntity == null){
            throw new UnknownResourceTypeException();
        }else{
            reportJpaRepository.save(reportEntity);
        }
    }

    @Override
    public boolean checkReportExists(Long resourceId, Long reporterId, ResourceType resourceType) {
        return reportJpaRepository.findById(ReportId.builder()
                .reporterId(reporterId)
                .resourceId(resourceId)
                .resourceType(resourceType)
                .build()).isPresent();
    }
}
