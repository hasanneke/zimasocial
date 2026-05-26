package com.zima.zimasocial.context.contentmoderation.report.instracture;

import com.zima.zimasocial.context.contentmoderation.report.ContentRepository;
import com.zima.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zima.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.domain.value.CommentId;
import com.zima.zimasocial.context.social2.domain.value.PostId;
import com.zima.zimasocial.context.social2.repository.CommentRepository;
import com.zima.zimasocial.context.social2.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ContentDBRepository implements ContentRepository {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepositoryDomain authorRepository;

    public ContentDBRepository(PostRepository postRepository, CommentRepository commentRepository, AuthorRepositoryDomain authorRepository) {
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
        Optional<Comment> comment = commentRepository.findById(new CommentId(id));
        return comment.map(value -> new CommentContent(value.getId().getValue(), value.getPostId().getValue(), value.getParentId().getValue(), value.getAuthorId()));
    }
}