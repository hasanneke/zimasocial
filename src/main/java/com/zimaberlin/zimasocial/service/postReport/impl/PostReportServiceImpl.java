package com.zimaberlin.zimasocial.service.postReport.impl;

import com.mchange.v2.util.ResourceClosedException;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.entity.postReport.PostReportEntity;
import com.zimaberlin.zimasocial.entity.postReport.PostReportEntityId;
import com.zimaberlin.zimasocial.entity.postReport.PostReportReason;
import com.zimaberlin.zimasocial.entity.postReport.ReportedPostType;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.PostReportReasonRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.service.postReport.PostReportService;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class PostReportServiceImpl implements PostReportService {
    private PostRepository postRepository;
    private PostReportReasonRepository postReportReasonRepository;
    @Override
    public void reportPost(Long postId, Long reasonId, ReportedPostType type) {
        PostReportReason postReportReason = postReportReasonRepository.findById(reasonId).orElseThrow(()-> new ResourceNotFoundException("Report reason not found"));
        UserEntity reporter = CurrentUser.getCurrentUserProfile();
        PostEntity post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        post.report(
                PostReportEntity.builder()
                        .id(new PostReportEntityId(postId, reporter.getId()))
                        .reportedUser(post.getUser())
                        .reportedPostType(type)
                        .postReportReason(postReportReason)
                        .reporter(reporter)
                        .build()
        );
    }
}
