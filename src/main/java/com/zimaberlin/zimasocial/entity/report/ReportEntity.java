package com.zimaberlin.zimasocial.entity.report;

import com.zimaberlin.zimasocial.entity.BaseEntity;
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

    @Column(name = "description", length = 512)
    private String description;

    public static ReportEntity buildPostReport(ReportRequest request, PostEntity post) {
        return ReportEntity.builder()
                .id(new ReportId(post.getId(), CurrentUser.getCurrentUserProfile().getId(), ResourceType.post))
                .reportedUser(post.getUser())
                .reportReason(request.getReason())
                .description(request.getDescription())
                .reporter(CurrentUser.getCurrentUserProfile())
                .build();
    }
    public static ReportEntity buildCommentReport(ReportRequest request, CommentEntity comment) {
        return ReportEntity.builder()
                .id(new ReportId(comment.getId(), CurrentUser.getCurrentUserProfile().getId(), ResourceType.comment))
                .reportedUser(comment.getUser())
                .reportReason(request.getReason())
                .description(request.getDescription())
                .reporter(CurrentUser.getCurrentUserProfile())
                .build();
    }
    public static ReportEntity buildProfileReport(ReportRequest request, UserEntity profile) {
        return ReportEntity.builder()
                .id(new ReportId(profile.getId(), CurrentUser.getCurrentUserProfile().getId(), ResourceType.profile))
                .reportedUser(profile)
                .reportReason(request.getReason())
                .description(request.getDescription())
                .reporter(CurrentUser.getCurrentUserProfile())
                .build();
    }
}
