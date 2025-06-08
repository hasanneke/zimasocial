package com.zimaberlin.zimasocial.context.social.api;

import com.zimaberlin.zimasocial.context.contentmoderation.report.ReportRepository;
import com.zimaberlin.zimasocial.context.social.api.view.AuthorAuthorViewAdapter;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorNotFoundException;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.like.Like;
import com.zimaberlin.zimasocial.context.social.like.LikeRepository;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.report.ResourceType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.views.post.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostViewAdapter {
    private final ReportRepository reportRepository;
    private final LikeRepository likeRepository;
    private final AuthorRepository authorRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;

    @Autowired
    public PostViewAdapter(ReportRepository reportRepository, LikeRepository likeRepository, AuthorRepository authorRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorRepository = authorRepository;
        this.reportRepository = reportRepository;
        this.likeRepository = likeRepository;
        this.authorViewAdapter = authorViewAdapter;
    }

    public PostView populated(Post post) {
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        boolean postReported = reportRepository.checkReportExists(post.getPostId(), authenticatedAuthor.getAuthorId(), ResourceType.post);
        Optional<Like> like = likeRepository.findByPostIdAndAuthorId(post.getPostId(), authenticatedAuthor.getAuthorId());

        PostView postView = new PostView();
        if(like.isPresent()){
            postView.setLiked(true);;
        }
        Author author = authorRepository.findById(post.getAuthorId()).orElse(null);
        postView.setLiked(like.isPresent());
        postView.setIsReported(postReported);
        postView.setUser(authorViewAdapter.authorViewFromAuthor(author));
        postView.setId( post.getPostId() );
        postView.setContent( post.getContent() );
        postView.setUrl( post.getUrl() );
        postView.setType( post.getType() );
        postView.setLikeCount( post.getLikeCount() );
        postView.setCommentCount( post.getCommentCount() );
        postView.setCreatedAt( post.getCreatedAt() );
        postView.setUpdatedAt( post.getUpdatedAt() );
        postView.addLinks();

        return postView;
    }

    public List<PostView> populated(List<Post> entities) {
        return entities.stream().map(this::populated).toList();
    }
}
