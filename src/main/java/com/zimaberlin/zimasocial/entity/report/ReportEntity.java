package com.zimaberlin.zimasocial.entity.report;

import com.zimaberlin.zimasocial.entity.BaseEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
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
public class ReportEntity extends BaseEntity {
    @EmbeddedId
    private ReportId id;

    @ManyToOne
    @JoinColumn(name = "reporter_id",insertable = false,updatable = false)
    private UserEntity reporter;

    @ManyToOne
    @JoinColumn(name = "reported_post_owner_id")
    private UserEntity reportedUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_reason", nullable = false)
    private ReportReason reportReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ReportType reportType;

    @Column(name = "description", length = 512)
    private String description;
}
