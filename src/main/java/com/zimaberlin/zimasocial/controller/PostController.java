package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.posts.PostServiceImpl;
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
@RequestMapping("/api/v1/posts")
@Tag(name = "Posts Management", description = "APIs for managing posts")
public class PostController {
    private final PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping
    public HttpEntity<PagedModel<PostView>> getPosts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "type", defaultValue = "any") PostType type,
            @RequestParam(name = "slug", required = false) String slug) throws NoSuchMethodException {
        Page<PostView> postPage;
        if(slug != null){
            postPage = postService.getPosts(page, size, slug, type);
        } else {
            postPage = postService.getPosts(page, size, type);
        }

        PagedModel<PostView> pagedModel = PagedModel.of(
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

        if(page + 1 < postPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, type, slug).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size, type, slug).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }

        return new HttpEntity<>(pagedModel);
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostView> getPost(@PathVariable Long postId) {
        PostView postView = postService.getPost(postId);
        return ResponseEntity.ok(postView);
    }

    @PostMapping
    public ResponseEntity<PostView> createPost(@Valid @RequestBody PostPayload payload) {
        PostView postView = postService.createPost(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(postView);
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<PostView> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{postId}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) throws BadRequestException {
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{postId}/comments")
    public HttpEntity<PagedModel<CommentView>> getComments(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @PathVariable Long postId) throws NoSuchMethodException {

        Page<CommentView> commentsPage = postService.getComments(page, size, postId);
        PagedModel<CommentView> pagedModel = PagedModel.of(
                commentsPage.getContent(),
                new PagedModel.PageMetadata(commentsPage.getSize(),
                        commentsPage.getNumber(),
                        commentsPage.getTotalElements(),
                        commentsPage.getTotalPages()));

        Method method = this.getClass().getMethod("getComments", Integer.class, Integer.class, Long.class);
        if(page + 1 < commentsPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, postId).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }
        if(page > 0){
            Link link = linkTo(method, page - 1, size, postId).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new HttpEntity<>(pagedModel);
    }

    @PostMapping(path = "/{postId}/comments")
    public ResponseEntity<CommentView> makeComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentPayload payload) {
        CommentView commentView = postService.commentPost(postId, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentView);
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    public ResponseEntity<CommentEntity> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        postService.deleteComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(path = "/{postId}/comments/{commentId}/replies")
    public ResponseEntity<CommentView> replyComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentPayload payload) {
        CommentView commentView = postService.replyComment(postId, commentId, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentView);
    }

    @GetMapping(path = "/{postId}/comments/{commentId}/like")
    public HttpEntity<Void> likeComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postService.likeComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}/unlike")
    public HttpEntity<Void> unlikeComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postService.unlikeComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{postId}/comments/{commentId}/replies")
    public HttpEntity<PagedModel<CommentView>> getCommentReplies(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) throws NoSuchMethodException {

        Page<CommentView> commentPage = postService.getCommentReplies(page, size, postId, commentId);
        PagedModel<CommentView> pagedModel = PagedModel.of(
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

    @DeleteMapping(path = "/{postId}/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<CommentView> deleteReply(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @PathVariable(name = "replyId") Long replyId) {
        CommentView commentView = postService.deleteReplyComment(postId, commentId, replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commentView);
    }
}