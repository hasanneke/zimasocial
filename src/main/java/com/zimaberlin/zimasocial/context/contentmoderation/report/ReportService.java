package com.zimaberlin.zimasocial.context.contentmoderation.report;

import com.zimaberlin.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zimaberlin.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zimaberlin.zimasocial.context.contentmoderation.report.exception.ReportAlreadyMadeException;
import com.zimaberlin.zimasocial.context.contentmoderation.report.reports.CommentReport;
import com.zimaberlin.zimasocial.context.contentmoderation.report.reports.PostReport;
import com.zimaberlin.zimasocial.context.contentmoderation.user.User;
import com.zimaberlin.zimasocial.context.contentmoderation.user.UserRepository;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final ContentRepository contentRepository;
    private final AuthorRepository authorRepository;
    @Autowired
    public ReportService(ReportRepository reportRepository, ContentRepository contentRepository, AuthorRepository authorRepository) {
        this.reportRepository = reportRepository;
        this.contentRepository = contentRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void reportPost(Long postId, ReportReason reason, String description) {
        Author authenticatedUser = authorRepository.getAuthenticatedAuthor();
        boolean reportExists = reportRepository.checkReportExists(postId, authenticatedUser.getId(), ResourceType.post);
        if(reportExists){
            throw new ReportAlreadyMadeException();
        }
        PostContent post = contentRepository.getPost(postId).orElseThrow(CommentNotFoundException::new);
        reportRepository.save(new PostReport(post.postId(), reason, authenticatedUser.getId(), post.authorId(), description));
    }
    @Transactional
    public void reportComment(Long commentId, ReportReason reason, String description) {
        Author authenticatedUser = authorRepository.getAuthenticatedAuthor();
        boolean reportExists = reportRepository.checkReportExists(commentId, authenticatedUser.getId(), ResourceType.comment);
        if(reportExists){
            throw new ReportAlreadyMadeException();
        }
        CommentContent comment = contentRepository.getComment(commentId).orElseThrow(CommentNotFoundException::new);
        reportRepository.save(new CommentReport(comment.commentId(), reason, authenticatedUser.getId(), comment.authorId(), description));
    }
}
