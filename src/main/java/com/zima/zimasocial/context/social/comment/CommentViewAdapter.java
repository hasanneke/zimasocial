package com.zima.zimasocial.context.social.comment;
import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.social.api.author.AuthorAuthorViewAdapter;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.like.LikeRepository;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.views.comment.CommentView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentViewAdapter {
    private final AuthorRepository authorRepository;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;

    public CommentViewAdapter(AuthorRepository authorRepository, LikeRepository likeRepository, ReportRepository reportRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorRepository = authorRepository;
        this.likeRepository = likeRepository;
        this.reportRepository = reportRepository;
        this.authorViewAdapter = authorViewAdapter;
    }

    public CommentView populated(Comment comment) {
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        // Create domain instance
        CommentView commentView = new CommentView();
        // UnProxy Proxies
        Author author = authorRepository.findById(comment.getAuthorId()).orElse(null);
        // Set domain values
        commentView.setContent(comment.getContent());
        commentView.setId(comment.getCommentId());
        commentView.setAuthor(authorViewAdapter.authorViewFromAuthor(author));
        commentView.setUpdatedAt(comment.getUpdatedAt());
        commentView.setCreatedAt(comment.getCreatedAt());
        commentView.setLikeCount(comment.getLikeCount());
        commentView.setReplyCount(comment.getReplyCount());
        commentView.setMediaId(comment.getMediaId());

        Optional<CommentLike> like = likeRepository.findByCommentIdAndAuthorId(comment.getCommentId(), authenticatedAuthor.getId());
        commentView.setIsLiked(like.isPresent());

        boolean isCommentReported = reportRepository.checkReportExists(comment.getCommentId(), authenticatedAuthor.getId(), ResourceType.comment);
        commentView.setIsReported(isCommentReported);
        return commentView;
    }
    public List<CommentView> populated(List<Comment> comments) {
        return comments.stream().map(this::populated).toList();
    }
}
