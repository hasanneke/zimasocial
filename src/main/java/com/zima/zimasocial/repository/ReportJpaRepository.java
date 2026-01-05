package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.report.ReportId;
import com.zima.zimasocial.entity.report.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportJpaRepository extends JpaRepository<ReportEntity, ReportId> {
    Optional<ReportEntity> findById(ReportId reportId);
}
