package com.zima.zimasocial.context.contentmoderation.report.instracture;

import com.zima.zimasocial.context.contentmoderation.report.ContentRepository;
import com.zima.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zima.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social.comment.CommentDomain;
import com.zima.zimasocial.context.social.comment.CommentDomainRepository;
import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import com.zima.zimasocial.context.social2.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ContentDBRepository implements ContentRepository {
    private final PostRepository postRepository;
    private final CommentDomainRepository commentRepository;
    private final AuthorRepositoryDomain authorRepository;

    public ContentDBRepository(PostRepository postRepository, CommentDomainRepository commentRepository, AuthorRepositoryDomain authorRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<PostContent> getPost(Long id) {
        Optional<Post> post = postRepository.findById(new PostId(id));
        return post.map(value -> new PostContent(value.getId().getValue(), value.getAuthorId()));
    }

    @Override
    public Optional<CommentContent> getComment(Long id) {
        Optional<CommentDomain> comment = commentRepository.findById(id);
        return comment.map(value -> new CommentContent(value.getCommentId(), value.getPostId(), value.getParentCommentId(), value.getAuthorId()));
    }
}