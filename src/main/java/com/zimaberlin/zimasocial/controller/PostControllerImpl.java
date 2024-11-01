package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.domain.Comment;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.service.Posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.Posts.PostServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Tag(name = "Posts Management", description = "APIs for managing posts")
public class PostControllerImpl implements PostController{
    private final PostServiceImpl postService;

    @Autowired
    public PostControllerImpl(PostServiceImpl postService) {
        this.postService = postService;
    }

    @Override
    public HttpEntity<PagedModel<Post>> getPosts(Integer page, Integer size, PostType type, String slug) throws NoSuchMethodException {
        Page<Post> postPage;
        if(slug != null){
            postPage = postService.getPosts(page, size, slug, type);
        }else{
            postPage = postService.getPosts(page, size, type);
        }

        PagedModel<Post> pagedModel = PagedModel.of(
                        postPage.getContent(),
                        new PagedModel.PageMetadata(postPage.getSize(),
                        postPage.getNumber(),
                        postPage.getTotalElements(),
                        postPage.getTotalPages()));

        Method method = this.getClass().getMethod("getPosts",
                Integer.class,
                Integer.class,
                PostType.class,
                String.class);

        if(page < postPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, type, slug).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size, type, slug).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }

        return new HttpEntity<>(pagedModel);
    }

    @Override
    public ResponseEntity<Post> getPost(Long postId) {
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @Override
    public ResponseEntity<Post> createPost( PostPayload payload) {
        Post post = postService.createPost(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @Override
    public ResponseEntity<Post> deletePost( Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> likePost(Long postId)  {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> unlikePost( Long postId) {
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<CommentEntity> makeComment(Long postId, CommentPayload payload){
        postService.commentPost(postId, payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<CommentEntity> deleteComment(Long postId, Long commentId){
        postService.deleteComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Comment> replyComment( Long postId,  Long commentId, CommentPayload payload){
        Comment comment = postService.replyComment(postId, commentId, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @Override
    public HttpEntity<PagedModel<Comment>> getComments(Integer page,  Integer size, Long postId) throws NoSuchMethodException {
        Page<Comment> commentsPage = postService.getComments(page, size, postId);
        PagedModel<Comment> pagedModel = PagedModel.of(
                commentsPage.getContent(),
                new PagedModel.PageMetadata(commentsPage.getSize(),
                        commentsPage.getNumber(),
                        commentsPage.getTotalElements(),
                        commentsPage.getTotalPages()));
        Method method = this.getClass().getMethod("getComments", Integer.class, Integer.class, Long.class);
        if(page < commentsPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, postId).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }
        if(page > 0){
            Link link = linkTo(method, page - 1, size, postId).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new HttpEntity<>(pagedModel);
    }

    @Override
    public ResponseEntity<Void> likeComment(Long postId, Long commentId) {
        postService.likeComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public HttpEntity<Void> unlikeComment(Long postId, Long commentId) {
        postService.unlikeComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public HttpEntity<PagedModel<Comment>> getCommentReplies(Integer page, Integer size, Long postId, Long commentId) throws NoSuchMethodException {
        Page<Comment> commentPage = postService.getCommentReplies(page, size, postId, commentId);
        PagedModel<Comment> pagedModel = PagedModel.of(
                        commentPage.getContent(),
                        new PagedModel.PageMetadata(commentPage.getSize(),
                        commentPage.getNumber(),
                        commentPage.getTotalElements(),
                        commentPage.getTotalPages())

        );
        Method method = this.getClass().getMethod("getCommentReplies", Integer.class, Integer.class, Long.class, Long.class);
        if(page < commentPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, postId, commentId).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size, postId, commentId).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }

        return new HttpEntity<>(pagedModel);
    }

    @Override
    public ResponseEntity<Comment> deleteReply(Long postId, Long commentId, Long replyId) {
        Comment comment = postService.deleteReplyComment(postId, commentId, replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(comment);
    }
}
