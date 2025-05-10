package com.zimaberlin.zimasocial.entity.report;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ReportId {
    @Column(name = "resource_id")
    private Long resourceId;
    @Column(name = "reporter_id")
    private Long reporterId;
    public ReportId(){}
    public ReportId(Long resourceId, Long reporterId) {
        this.resourceId = resourceId;
        this.reporterId = reporterId;
    }
}
