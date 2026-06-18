package com.zima.zimasocial.context.social.post.api.adapter;
import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.social.author.api.adapter.AuthorViewAdapter;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.post.entity.Comment;
import com.zima.zimasocial.context.social.post.entity.Like;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.post.repository.LikeRepository;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.LikeType;
import com.zima.zimasocial.context.contentmoderation.ResourceType;
import com.zima.zimasocial.context.social.post.api.views.CommentView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentViewAdapter {
    private final AuthorRepository authorRepository;
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;
    private final AuthorViewAdapter authorViewAdapter;

    public CommentViewAdapter(AuthorRepository authorRepository, LikeRepository likeRepository, ReportRepository reportRepository, AuthorViewAdapter authorViewAdapter) {
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
        commentView.setId(comment.getId().getValue());
        if(author != null) commentView.setAuthor(authorViewAdapter.toRichView(author));
        commentView.setUpdatedAt(comment.getUpdatedAt());
        commentView.setCreatedAt(comment.getCreatedAt());
        commentView.setLikeCount(comment.getLikeCount());
        commentView.setReplyCount(comment.getReplyCount());
        if(comment.getMediaId() != null) commentView.setMediaId(comment.getMediaId().getValue());
        Optional<Like> like = likeRepository.findByAuthorIdAndCommentIdAndType(authenticatedAuthor.getId(), new CommentId(comment.getId().getValue()), LikeType.comment);
        commentView.setIsLiked(like.isPresent());

        boolean isCommentReported = reportRepository.checkReportExists(comment.getId().getValue(), new AuthorId(authenticatedAuthor.getId().getValue()), ResourceType.comment);
        commentView.setIsReported(isCommentReported);
        return commentView;
    }

    public List<CommentView> populated(List<Comment> comments) {
        return comments.stream().map(this::populated).toList();
    }
}
