package com.zima.zimasocial.context.contentmoderation.report;

import com.zima.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zima.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zima.zimasocial.context.contentmoderation.report.exception.ReportAlreadyMadeException;
import com.zima.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zima.zimasocial.context.contentmoderation.report.reports.PostReport;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.entity.report.ReportReason;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final ContentRepository contentRepository;
    private final AuthorRepositoryDomain authorRepository;
    @Autowired
    public ReportService(ReportRepository reportRepository, ContentRepository contentRepository, AuthorRepositoryDomain authorRepository) {
        this.reportRepository = reportRepository;
        this.contentRepository = contentRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void reportPost(Long postId, ReportReason reason, String description) {
        AuthorDomain authenticatedUser = authorRepository.getAuthenticatedAuthor();
        boolean reportExists = reportRepository.checkReportExists(postId, authenticatedUser.getId(), ResourceType.post);
        if(reportExists){
            throw new ReportAlreadyMadeException();
        }
        PostContent post = contentRepository.getPost(postId).orElseThrow(CommentNotFoundException::new);
        reportRepository.save(new PostReport(post.postId(), reason, authenticatedUser.getId(), post.authorId(), description));
    }
    @Transactional
    public void reportComment(Long commentId, ReportReason reason, String description) {
        AuthorDomain authenticatedUser = authorRepository.getAuthenticatedAuthor();
        boolean reportExists = reportRepository.checkReportExists(commentId, authenticatedUser.getId(), ResourceType.comment);
        if(reportExists){
            throw new ReportAlreadyMadeException();
        }
        CommentContent comment = contentRepository.getComment(commentId).orElseThrow(CommentNotFoundException::new);
        reportRepository.save(new CommentReport(comment.commentId(), reason, authenticatedUser.getId(), comment.authorId(), description));
    }
}
