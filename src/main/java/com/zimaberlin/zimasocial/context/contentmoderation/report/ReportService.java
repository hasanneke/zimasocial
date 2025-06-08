package com.zimaberlin.zimasocial.context.contentmoderation.report;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, PostRepository postRepository, CommentRepository commentRepository, AuthorRepository authorRepository) {
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authorRepository = authorRepository;
    }

    public void report(ReportRequest request) {
        Author authenticatedUser = authorRepository.getAuthenticatedAuthor();
        boolean reportExists = reportRepository.checkReportExists(request.getResourceId(), authenticatedUser.getAuthorId(), request.getResourceType());
        if(reportExists){
            throw new ReportAlreadyMadeException();
        }
        Long reportedAuthorId;
        switch (request.getResourceType()){
            case ResourceType.post -> {
                Post post = postRepository.findById(request.getResourceId()).orElseThrow(PostNotFoundException::new);
                reportedAuthorId = post.getAuthorId();
            }
            case ResourceType.comment -> {
                Comment comment = commentRepository.findById(request.getResourceId()).orElseThrow(CommentNotFoundException::new);
                reportedAuthorId =  comment.getAuthorId();
            }
            case ResourceType.profile -> {
                reportedAuthorId = request.getResourceId();
            }
            default -> reportedAuthorId = null;
        }
        Report savedReport = new Report(request.getResourceId(), authenticatedUser.getAuthorId(), reportedAuthorId, request.getResourceType(), request.getReason(), request.getDescription());
        reportRepository.save(savedReport);
    }
}
