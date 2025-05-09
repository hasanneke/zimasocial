package com.zimaberlin.zimasocial.service.postReport;

import com.zimaberlin.zimasocial.entity.postReport.PostReportReason;
import com.zimaberlin.zimasocial.entity.postReport.ReportedPostType;

public interface PostReportService {
    void reportPost(Long postId, Long reasonId, ReportedPostType type);
}
