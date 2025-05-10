package com.zimaberlin.zimasocial.service.report;

import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ReportType;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;

public interface ReportService {
    void report(ReportRequest request);
}
