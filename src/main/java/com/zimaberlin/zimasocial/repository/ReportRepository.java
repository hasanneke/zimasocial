package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, ReportId> {
    Optional<ReportEntity> findById(ReportId reportId);
}
