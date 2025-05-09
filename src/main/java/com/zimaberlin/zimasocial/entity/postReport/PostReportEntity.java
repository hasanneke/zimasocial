package com.zimaberlin.zimasocial.entity.postReport;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "POST_REPORT")
@Builder
@AllArgsConstructor
@SQLRestriction(value = "IS_DELETED IS FALSE")
@Getter
public class PostReportEntity {
    @EmbeddedId
    private PostReportEntityId id;

    @ManyToOne
    @JoinColumn(name = "reporter_id",insertable = false,updatable = false)
    private UserEntity reporter;

    @ManyToOne
    @JoinColumn(name = "reported_post_id", insertable = false, updatable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "reported_post_owner_id")
    private UserEntity reportedUser;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "report_reason_id", nullable = false)
    private PostReportReason postReportReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportedPostType reportedPostType;
}
