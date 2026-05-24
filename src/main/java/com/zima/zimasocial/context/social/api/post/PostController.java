package com.zima.zimasocial.context.social.api.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zima.zimasocial.aop.ResourceAcess.HasCommentAccess;
import com.zima.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zima.zimasocial.context.social.api.FeedFilterPlain;
import com.zima.zimasocial.context.social.api.dto.LikeDTO;
import com.zima.zimasocial.context.social.comment.CommentDomain;
import com.zima.zimasocial.context.social.comment.CommentViewAdapter;
import com.zima.zimasocial.context.social.post.application.PostScorePunisherService;
import com.zima.zimasocial.context.social.post.application.PostService;
import com.zima.zimasocial.context.social.post.repository.PostSortType;
import com.zima.zimasocial.service.posts.Payload.CommentPayload;
import com.zima.zimasocial.service.posts.Payload.PostPayload;
import com.zima.zimasocial.views.comment.CommentView;
import com.zima.zimasocial.views.post.PostView;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/posts")
@Tag(name = "Posts Controller", description = "APIs for managing posts")
@Validated
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostScorePunisherService postScorePunisherService;
    private final PostControllerBridge postControllerBridge;
    private final CommentViewAdapter commentViewAdapter;
    private static final DateTimeFormatter CLIENT_TS_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");


    @GetMapping
    public String punishPosts() {
        postScorePunisherService.punishPosts();
        return "";
    }

    @PostMapping
    public ResponseEntity<PostView> createPost(@Valid @RequestBody PostPayload payload) throws JsonProcessingException {
        return ResponseEntity.ok(postControllerBridge.createPost(payload));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostView>> getPostsPaginated(
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @Max(20) @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "lastScore", required = false) Integer lastScore,
            @Positive @RequestParam(name = "lastId", required = false) Long lastId,
            @RequestParam(name = "type", required = false) PostCategory category,
            @Size(max = 30) @RequestParam(name = "slug", required = false) String slug,
            @RequestParam(name = "sortType", required = false) PostSortType sortType) {
        return ResponseEntity.ok(postService.getFeed(FeedFilterPlain.builder()
                .offset(offset)
                .size(size == null ? 20 : size)
                .lastScore(lastScore)
                .lastPostId(lastId)
                .category(category)
                .slug(slug)
                .sortType(sortType)
                .build()));
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostView> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postControllerBridge.getPost(postId));
    }

    @DeleteMapping(path = "/{postId}")
    @HasPostAccess(idParameterName = "postId")
    public ResponseEntity<PostView> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.like(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/{postId}/likes")
    public ResponseEntity<Page<LikeDTO>> getPostLikes(
            @PathVariable Long postId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(postService.getAllPostLikes(postId, PageRequest.of(page, size)));
    }

    @DeleteMapping(path = "/{postId}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
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
        CommentDomain comment = postService.comment(postId, payload.getContent(), payload.getMediaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentViewAdapter.populated(comment));
    }
    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    @HasCommentAccess(idParameterName = "commentId")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        postService.removeComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{postId}/comments/{commentId}/like")
    public HttpEntity<Void> likeComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postService.likeComment(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/{postId}/comments/{commentId}/likes")
    public HttpEntity<Page<LikeDTO>> getCommentLikes(
            @PathVariable(name = "commentId") Long commentId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return ResponseEntity.ok(postService.getAllCommentLikes(commentId, PageRequest.of(page, size)));
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}/unlike")
    public HttpEntity<Void> unlikeComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postService.unlikeComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping(path = "/{postId}/comments/{commentId}/replies")
    public ResponseEntity<CommentView> replyComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentPayload payload) {
        CommentDomain comment = postService.replyComment(commentId, payload.getContent(), payload.getMediaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentViewAdapter.populated(comment));
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<CommentView> deleteReply(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @PathVariable(name = "replyId") Long replyId) {
        postService.deleteReply(replyId);
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
