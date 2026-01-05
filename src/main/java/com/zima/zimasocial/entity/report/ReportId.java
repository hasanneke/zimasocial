package com.zima.zimasocial.entity.report;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
public class ReportId {
    @Column(name = "resource_id")
    private Long resourceId;
    @Column(name = "reporter_id")
    private Long reporterId;
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceType resourceType;
    public ReportId(Long resourceId, Long reporterId, ResourceType resourceType) {
        this.resourceId = resourceId;
        this.reporterId = reporterId;
        this.resourceType = resourceType;
    }
}
