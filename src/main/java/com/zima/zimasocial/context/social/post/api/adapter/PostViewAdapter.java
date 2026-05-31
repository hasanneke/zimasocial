package com.zima.zimasocial.context.social.post.api.adapter;

import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.social.author.api.adapter.AuthorViewAdapter;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.post.api.views.PostView;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.post.entity.Like;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.post.repository.LikeRepository;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostViewAdapter {
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;
    private final AuthorRepository authorRepository;
    private final AuthorViewAdapter authorViewAdapter;
    public PostView toView(Post post) {
        Author viewerAuthor = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        PostView postView = new PostView();
        boolean postReported = reportRepository.checkReportExists(post.getId().getValue(), new AuthorId(viewerAuthor.getId().getValue()), ResourceType.post);
        Optional<Like> like = likeRepository.findByAuthorIdAndPostIdAndType(viewerAuthor.getId(), post.getId(), LikeType.post);
        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(AuthorNotFoundException::new);

        postView.setUser(authorViewAdapter.toRichView(author));
        postView.setLiked(like.isPresent());
        postView.setIsReported(postReported);
        postView.setId( post.getId().getValue() );
        postView.setContent( post.getContent().getText() );
        postView.setType( post.getContent().getType() );
        postView.setLikeCount( post.getStats().getLikeCount() );
        postView.setCommentCount( post.getStats().getCommentCount() );
        postView.setCreatedAt( post.getCreatedAt() );
        postView.setUpdatedAt( post.getUpdatedAt() );
        postView.setMediaId( post.getMediaId() != null ? post.getMediaId().getValue() : null);
        return postView;
    }
}
