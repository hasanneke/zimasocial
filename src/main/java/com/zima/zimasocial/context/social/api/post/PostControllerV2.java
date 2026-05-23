package com.zima.zimasocial.context.social.api.post;

import com.zima.zimasocial.context.social2.application.PostApplicationService;
import com.zima.zimasocial.service.posts.Payload.CommentPayload;
import com.zima.zimasocial.views.comment.CommentView;
import com.zima.zimasocial.views.post.PostView;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v2/posts")
@Tag(name = "Posts Controller", description = "APIs for managing posts")
@Validated
@RequiredArgsConstructor
public class PostControllerV2 {
    private final PostApplicationService postService;

    @GetMapping(path = "/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{postId}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<PostView> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{postId}/comments")
    public ResponseEntity<CommentView> makeComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentPayload payload) {
        CommentView comment = postService.comment(postId, payload.getContent(), payload.getMediaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        postService.removeComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
