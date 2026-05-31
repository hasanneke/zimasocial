package com.zima.zimasocial.context.contentmoderation.report.infastructure;

import com.zima.zimasocial.context.contentmoderation.report.ContentRepository;
import com.zima.zimasocial.context.contentmoderation.report.content.CommentContent;
import com.zima.zimasocial.context.contentmoderation.report.content.PostContent;
import com.zima.zimasocial.context.social.post.entity.Comment;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.post.repository.CommentRepository;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContentDBRepository implements ContentRepository {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;

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