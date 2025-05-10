package com.zimaberlin.zimasocial.service.report.impl;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ReportType;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.repository.ReportRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.report.ReportService;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
import com.zimaberlin.zimasocial.service.report.exception.ReportResourceTypeNotFound;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    @Autowired
    public ReportServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ReportRepository reportRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void report(ReportRequest request) {
        Long resourceId = request.getResourceId();
        ReportReason reason = request.getReason();
        ReportType type = request.getReportType();
        String description = request.getDescription();
        UserEntity reporter = CurrentUser.getCurrentUserProfile();
        ReportEntity report;
        switch (request.getReportType()){
            case ReportType.post -> {
                PostEntity post = postRepository.findById(request.getResourceId()).orElseThrow(PostNotFoundException::new);
                report = ReportEntity.builder()
                        .id(new ReportId(post.getId(), reporter.getId()))
                        .reportedUser(post.getUser())
                        .reportType(type)
                        .reportReason(reason)
                        .description(description)
                        .reporter(reporter)
                        .build();
            }
            case ReportType.comment -> {
                CommentEntity comment = commentRepository.findById(resourceId).orElseThrow(CommentNotFoundException::new);
                report = ReportEntity.builder()
                        .id(new ReportId(comment.getId(), reporter.getId()))
                        .reportedUser(comment.getUser())
                        .reportType(type)
                        .reportReason(reason)
                        .description(description)
                        .reporter(reporter)
                        .build();
            }case ReportType.profile -> {
                UserEntity user = userRepository.findById(resourceId).orElseThrow(UserNotFoundException::new);
                report = ReportEntity.builder()
                        .id(new ReportId(user.getId(), reporter.getId()))
                        .reportedUser(user)
                        .reportType(type)
                        .reportReason(reason)
                        .description(description)
                        .reporter(reporter)
                        .build();
            } case null -> {
                throw new ReportResourceTypeNotFound();
            }
        }
        reportRepository.save(report);
    }
}
