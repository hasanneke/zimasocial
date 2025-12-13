package com.zimaberlin.zimasocial.context.contentmoderation.report.instracture;

import com.zimaberlin.zimasocial.context.contentmoderation.report.ContentRepository;
import com.zimaberlin.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zimaberlin.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.context.social.post.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ContentDBRepository implements ContentRepository {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;

    public ContentDBRepository(PostRepository postRepository, CommentRepository commentRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<PostContent> getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(value -> new PostContent(value.getPostId(), value.getAuthorId()));
    }

    @Override
    public Optional<CommentContent> getComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(value -> new CommentContent(value.getCommentId(), value.getPostId(), value.getParentCommentId(), value.getAuthorId()));
    }
}