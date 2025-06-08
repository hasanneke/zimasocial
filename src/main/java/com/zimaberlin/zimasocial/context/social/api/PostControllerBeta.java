package com.zimaberlin.zimasocial.context.social.api;

import com.zimaberlin.zimasocial.aop.ResourceAcess.HasCommentAccess;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.comment.CommentViewAdapter;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.context.social.post.PostServiceBeta;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.service.posts.PostService;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "/api/v2/posts")
@Tag(name = "Posts Controller Beta", description = "APIs for managing posts")
public class PostControllerBeta {
    private final PostServiceBeta postServiceBeta;
    private final PostControllerBridge postControllerBridge;
    private final CommentViewAdapter commentViewAdapter;
    @Autowired
    public PostControllerBeta(PostControllerBridge postControllerBridge, PostServiceBeta postServiceBeta, CommentViewAdapter commentViewAdapter) {
        this.postControllerBridge = postControllerBridge;
        this.postServiceBeta = postServiceBeta;
        this.commentViewAdapter = commentViewAdapter;
    }

    @PostMapping
    public ResponseEntity<PostView> createPost(@Valid @RequestBody PostPayload payload) {
        return ResponseEntity.ok(postControllerBridge.createPost(payload));
    }

    @GetMapping
    public HttpEntity<PagedModel<PostView>> getPosts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "type", required = false) PostType type,
            @RequestParam(name = "slug", required = false) String slug) throws NoSuchMethodException {
        return new HttpEntity<>(postControllerBridge.getPosts(page, size, type, slug));
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostView> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postControllerBridge.getPost(postId));
    }

    @DeleteMapping(path = "/{postId}")
    @HasPostAccess(idParameterName = "postId")
    public ResponseEntity<PostView> deletePost(@PathVariable Long postId) {
        postServiceBeta.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postServiceBeta.like(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{postId}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postServiceBeta.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{postId}/comments")
    public HttpEntity<PagedModel<CommentView>> getComments(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @PathVariable Long postId) throws NoSuchMethodException {
        return new HttpEntity<>(postControllerBridge.getComments(page, size, postId));
    }
    @PostMapping(path = "/{postId}/comments")
    public ResponseEntity<CommentView> makeComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentPayload payload) {
        Comment comment = postServiceBeta.comment(postId, payload.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentViewAdapter.populated(comment));
    }
    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    @HasCommentAccess(idParameterName = "commentId")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        postServiceBeta.removeComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{postId}/comments/{commentId}/like")
    public HttpEntity<Void> likeComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postServiceBeta.likeComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}/unlike")
    public HttpEntity<Void> unlikeComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postServiceBeta.unlikeComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping(path = "/{postId}/comments/{commentId}/replies")
    public ResponseEntity<CommentView> replyComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentPayload payload) {
        Comment comment = postServiceBeta.replyComment(postId, commentId, payload.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentViewAdapter.populated(comment));
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<CommentView> deleteReply(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @PathVariable(name = "replyId") Long replyId) {
        postServiceBeta.deleteReply(replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{postId}/comments/{commentId}/replies")
    public HttpEntity<PagedModel<CommentView>> getCommentReplies(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @PathVariable(name = "commentId") Long commentId) throws NoSuchMethodException {
        return new HttpEntity<>(postControllerBridge.getCommentReplies(page, size, commentId));
    }

    @GetMapping(path = "/todays-posts")
    public ResponseEntity<List<PostView>> getTodaysPosts() {
        return ResponseEntity.ok(postControllerBridge.getTodaysPosts());
    }
}
