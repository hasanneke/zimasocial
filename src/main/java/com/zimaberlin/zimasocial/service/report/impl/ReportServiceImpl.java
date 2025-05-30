package com.zimaberlin.zimasocial.service.report.impl;

import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.report.ReportId;
import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.repository.ReportRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.report.ReportService;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
import com.zimaberlin.zimasocial.service.report.exception.ReportAlreadyMadeException;
import com.zimaberlin.zimasocial.service.report.exception.ReportResourceTypeNotFound;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<ReportEntity> checkReportExists = reportRepository.findById(new ReportId(request.getResourceId(), CurrentUser.getCurrentUserProfile().getId(), request.getResourceType()));
        if(checkReportExists.isPresent()){
            throw new ReportAlreadyMadeException();
        }
        Long resourceId = request.getResourceId();
        ReportEntity report;
        switch (request.getResourceType()){
            case ResourceType.post -> {
                PostEntity post = postRepository.findById(resourceId).orElseThrow(PostNotFoundException::new);
                report = ReportEntity.buildPostReport(request, post);
            }
            case ResourceType.comment -> {
                CommentEntity comment = commentRepository.findById(resourceId).orElseThrow(CommentNotFoundException::new);
                report = ReportEntity.buildCommentReport(request, comment);
            }case ResourceType.profile -> {
                UserEntity user = userRepository.findById(resourceId).orElseThrow(UserNotFoundException::new);
                report = ReportEntity.buildProfileReport(request, user);
            } case null -> {
                throw new ReportResourceTypeNotFound();
            }
        }
        reportRepository.save(report);
    }
}
