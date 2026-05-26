package com.zima.zimasocial.context.social.comment;
import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.social.api.author.AuthorAuthorViewAdapter;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.like.LikeDomainRepository;
import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.context.social2.api.views.CommentView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentViewAdapter {
    private final AuthorRepositoryDomain authorRepository;
    private final LikeDomainRepository likeRepository;
    private final ReportRepository reportRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;

    public CommentViewAdapter(AuthorRepositoryDomain authorRepository, LikeDomainRepository likeRepository, ReportRepository reportRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorRepository = authorRepository;
        this.likeRepository = likeRepository;
        this.reportRepository = reportRepository;
        this.authorViewAdapter = authorViewAdapter;
    }

    public CommentView populated(Comment comment) {
        AuthorDomain authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        // Create domain instance
        CommentView commentView = new CommentView();
        // UnProxy Proxies
        AuthorDomain author = authorRepository.findById(new AuthorDomainId(comment.getAuthorId().getValue())).orElse(null);
        // Set domain values
        commentView.setContent(comment.getContent());
        commentView.setId(comment.getId().getValue());
        commentView.setAuthor(authorViewAdapter.authorViewFromAuthor(author));
        commentView.setUpdatedAt(comment.getUpdatedAt());
        commentView.setCreatedAt(comment.getCreatedAt());
        commentView.setLikeCount(comment.getLikeCount());
        commentView.setReplyCount(comment.getReplyCount());
        if(comment.getMediaId() != null) commentView.setMediaId(comment.getMediaId().getValue());
        Optional<CommentLike> like = likeRepository.findByCommentIdAndAuthorId(comment.getId().getValue(), authenticatedAuthor.getId());
        commentView.setIsLiked(like.isPresent());

        boolean isCommentReported = reportRepository.checkReportExists(comment.getId().getValue(), new AuthorId(authenticatedAuthor.getId().getValue()), ResourceType.comment);
        commentView.setIsReported(isCommentReported);
        return commentView;
    }

    public List<CommentView> populated(List<Comment> comments) {
        return comments.stream().map(this::populated).toList();
    }
}
