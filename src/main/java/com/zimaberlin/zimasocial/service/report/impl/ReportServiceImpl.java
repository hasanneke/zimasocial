//package com.zimaberlin.zimasocial.service.report.impl;
//
//import com.zimaberlin.zimasocial.entity.CommentEntity;
//import com.zimaberlin.zimasocial.entity.PostEntity;
//import com.zimaberlin.zimasocial.entity.user.UserEntity;
//import com.zimaberlin.zimasocial.entity.report.ReportEntity;
//import com.zimaberlin.zimasocial.entity.report.ReportId;
//import com.zimaberlin.zimasocial.entity.report.ResourceType;
//import com.zimaberlin.zimasocial.repository.CommentJpaRepository;
//import com.zimaberlin.zimasocial.repository.PostJpaRepository;
//import com.zimaberlin.zimasocial.repository.ReportJpaRepository;
//import com.zimaberlin.zimasocial.repository.UserRepository;
//import com.zimaberlin.zimasocial.service.report.ReportService;
//import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
//import com.zimaberlin.zimasocial.context.contentmoderation.report.ReportAlreadyMadeException;
//import com.zimaberlin.zimasocial.service.report.exception.ReportDataTypeNotFound;
//import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
//import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
//import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
//import com.zimaberlin.zimasocial.utility.CurrentUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class ReportServiceImpl implements ReportService {
//    private final PostJpaRepository postJpaRepository;
//    private final CommentJpaRepository commentJpaRepository;
//    private final ReportJpaRepository reportRepository;
//    private final UserRepository userRepository;
//    @Autowired
//    public ReportServiceImpl(PostJpaRepository postJpaRepository, CommentJpaRepository commentJpaRepository, ReportJpaRepository reportRepository, UserRepository userRepository) {
//        this.postJpaRepository = postJpaRepository;
//        this.commentJpaRepository = commentJpaRepository;
//        this.reportRepository = reportRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void report(ReportRequest request) {
//        Optional<ReportEntity> checkReportExists = reportRepository.findById(new ReportId(request.getResourceId(), CurrentUser.getCurrentUserProfile().getId(), request.getResourceType()));
//        if(checkReportExists.isPresent()){
//            throw new ReportAlreadyMadeException();
//        }
//        Long resourceId = request.getResourceId();
//        ReportEntity report;
//        switch (request.getResourceType()){
//            case ResourceType.post -> {
//                PostEntity post = postJpaRepository.findById(resourceId).orElseThrow(PostNotFoundException::new);
//                report = ReportEntity.buildPostReport(request, post);
//            }
//            case ResourceType.comment -> {
//                CommentEntity comment = commentJpaRepository.findById(resourceId).orElseThrow(CommentNotFoundException::new);
//                report = ReportEntity.buildCommentReport(request, comment);
//            }case ResourceType.profile -> {
//                UserEntity user = userRepository.findById(resourceId).orElseThrow(UserNotFoundException::new);
//                report = ReportEntity.buildProfileReport(request, user);
//            } case null -> {
//                throw new ReportDataTypeNotFound();
//            }
//        }
//        reportRepository.save(report);
//    }
//}
