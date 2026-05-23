package com.zima.zimasocial.context.contentmoderation.report.instracture;

import com.zima.zimasocial.context.contentmoderation.report.ContentRepository;
import com.zima.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zima.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.comment.CommentDomain;
import com.zima.zimasocial.context.social.comment.CommentDomainRepository;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social.post.repository.PostDomainRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ContentDBRepository implements ContentRepository {
    private final PostDomainRepository postRepository;
    private final CommentDomainRepository commentRepository;
    private final AuthorRepository authorRepository;

    public ContentDBRepository(PostDomainRepository postRepository, CommentDomainRepository commentRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<PostContent> getPost(Long id) {
        Optional<PostDomain> post = postRepository.findById(id);
        return post.map(value -> new PostContent(value.getPostId(), value.getAuthorId()));
    }

    @Override
    public Optional<CommentContent> getComment(Long id) {
        Optional<CommentDomain> comment = commentRepository.findById(id);
        return comment.map(value -> new CommentContent(value.getCommentId(), value.getPostId(), value.getParentCommentId(), value.getAuthorId()));
    }
}