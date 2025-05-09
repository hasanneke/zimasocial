package com.zimaberlin.zimasocial.entity.postReport;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@RequiredArgsConstructor
@AllArgsConstructor
public class PostReportEntityId {
    @Column(name = "reported_post_id")
    private Long postId;
    @Column(name = "reporter_id")
    private Long reporterId;
}
