package com.zima.zimasocial.context.social.api.post;
import com.zima.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zima.zimasocial.context.social.api.author.AuthorAuthorViewAdapter;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.like.LikeDomain;
import com.zima.zimasocial.context.social.like.LikeDomainRepository;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.entity.report.ResourceType;
import com.zima.zimasocial.views.post.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostViewAdapter {
    private final ReportRepository reportRepository;
    private final LikeDomainRepository likeRepository;
    private final AuthorRepository authorRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;

    @Autowired
    public PostViewAdapter(ReportRepository reportRepository, LikeDomainRepository likeRepository, AuthorRepository authorRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorRepository = authorRepository;
        this.reportRepository = reportRepository;
        this.likeRepository = likeRepository;
        this.authorViewAdapter = authorViewAdapter;
    }

    public PostView populated(PostDomain post) {
        AuthorDomain authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        boolean postReported = reportRepository.checkReportExists(post.getPostId(), authenticatedAuthor.getId(), ResourceType.post);
        Optional<LikeDomain> like = likeRepository.findByPostIdAndAuthorId(post.getPostId(), authenticatedAuthor.getId());

        PostView postView = new PostView();
        if(like.isPresent()){
            postView.setLiked(true);;
        }
        AuthorDomain author = authorRepository.findById(post.getAuthorId()).orElse(null);
        postView.setLiked(like.isPresent());
        postView.setIsReported(postReported);
        postView.setUser(authorViewAdapter.authorViewFromAuthor(author));
        postView.setId( post.getPostId() );
        postView.setContent( post.getContent().content() );
        postView.setType( post.getContent().type() );
        postView.setLikeCount( post.getLikeCount() );
        postView.setCommentCount( post.getCommentCount() );
        postView.setCreatedAt( post.getCreatedAt() );
        postView.setUpdatedAt( post.getUpdatedAt() );
        postView.setMediaId( post.getContent().mediaId() != null ? post.getContent().mediaId().value() : null );

        return postView;
    }

    public List<PostView> populated(List<PostDomain> entities) {
        return entities.stream().map(this::populated).toList();
    }
}
