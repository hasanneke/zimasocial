package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.domain.Comment;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.service.Posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/posts")
public interface PostController {
    @Operation(
            summary = "Get posts",
            description = "Users can get posts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post are fetched successfully"
                    )
            }
    )
    @GetMapping
    HttpEntity<PagedModel<Post>> getPosts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "type", defaultValue = "any") PostType type,
            @RequestParam(name = "slug", required = false) String slug) throws NoSuchMethodException;



    @GetMapping(path = "/{postId}")
    ResponseEntity<Post> getPost(@PathVariable Long postId);

    @Operation(
            summary = "Create a post",
            description = "Users can create posts",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Post is created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            }
    )
    @PostMapping
    ResponseEntity<Post> createPost(@Valid @RequestBody PostPayload payload);

    @Operation(
            summary = "Delete a post",
            description = "Users can delete post",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post is deleted succesfully"),
                    @ApiResponse(responseCode = "404", description = "Post not found"),
                    @ApiResponse(responseCode = "403", description = "User is not owner of the post")
            }
    )
    @DeleteMapping(path = "/{postId}")
    ResponseEntity<Post> deletePost(@PathVariable Long postId);

    @Operation(
            summary = "Like a post",
            description = "Users can like post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Post liked succesfully"),
                    @ApiResponse(responseCode = "409", description = "Post is already liked"),
                    @ApiResponse(responseCode = "404", description = "Post not found")
            }
    )
    @PostMapping(path = "/{postId}/likes")
    ResponseEntity<Void> likePost(@PathVariable Long postId);

    @Operation(
            summary = "Unlike a post",
            description = "Users can unlike post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Post unliked succesfully"),
                    @ApiResponse(responseCode = "409", description = "Post is already unliked"),
                    @ApiResponse(responseCode = "404", description = "Post not found")
            }
    )
    @DeleteMapping(path = "/{postId}/likes")
    ResponseEntity<Void> unlikePost(@PathVariable Long postId) throws BadRequestException;

    @GetMapping(path = "/{postId}/comments")
    HttpEntity<PagedModel<Comment>> getComments(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @PathVariable Long postId) throws NoSuchMethodException;


    @Operation(summary = "Users make comments on Posts")
    @PostMapping(path = "/{postId}/comments")
    ResponseEntity<CommentEntity> makeComment(@PathVariable Long postId, @Valid @RequestBody CommentPayload payload);

    @Operation(summary = "Users deletes comment")
    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    ResponseEntity<CommentEntity> deleteComment(@PathVariable Long postId, @PathVariable Long commentId);

    @Operation(summary = "Users reply to comments")
    @PostMapping(path = "/{postId}/comments/{commentId}/replies")
    ResponseEntity<Comment> replyComment(@PathVariable Long postId,
                                         @PathVariable Long commentId,
                                         @Valid @RequestBody CommentPayload payload);

    @PostMapping(path = "/{postId}/comments/{commentId}/likes")
    HttpEntity<Void> likeComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "commentId") Long commentId);

    @DeleteMapping(path = "/{postId}/comments/{commentId}/likes")
    HttpEntity<Void> unlikeComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "commentId") Long commentId);

    @GetMapping(path = "/{postId}/comments/{commentId}/replies")
    HttpEntity<PagedModel<Comment>> getCommentReplies(
            @RequestParam(name = "page", defaultValue="0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) throws NoSuchMethodException;

    @DeleteMapping(path = "/{postId}/comments/{commentId}/replies/{replyId}")
    ResponseEntity<Comment> deleteReply(@PathVariable(name = "postId") Long postId,
                                        @PathVariable(name = "commentId") Long commentId,
                                        @PathVariable(name = "replyId") Long replyId);
}