package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.Posts.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("api/v1/posts")
@AllArgsConstructor
@RequiredArgsConstructor
@Tag(name = "Posts Management", description = "APIs for managing posts")
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    // GET POSTS
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
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<PagedModel<Post>> getPosts(@RequestParam(name = "page", defaultValue="0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                 @RequestParam(name = "type", defaultValue = "any") PostType type) throws NoSuchMethodException {
        Page<Post> postPage = postService.getPosts(page, size, type);
        PagedModel<Post> pagedModel = PagedModel.of(
                        postPage.getContent(),
                        new PagedModel.PageMetadata(postPage.getSize(),
                        postPage.getNumber(),
                        postPage.getTotalElements(),
                        postPage.getTotalPages()));

        Method method = this.getClass().getMethod("getPosts",
                Integer.class,
                Integer.class,
                PostType.class);

        if(page < postPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, type).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size, type).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }

        return new HttpEntity<>(pagedModel);
    }

    // CREATE POST
    @Operation(
            summary = "Create a post",
            description = "Users can create posts",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Post is created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> createPost(@RequestBody PostPayload payload) {
        Post post= postService.createPost(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    // DELETE POST
    @Operation(
            summary = "Delete a post",
            description = "Users can delete post",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Post is deleted succesfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User is not owner of the post"
                    )
            }
    )
    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // LIKE POST
    @Operation(
            summary = "Like a post",
            description = "Users can like post",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Post liked succesfully"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Post is already liked"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found"
                    )
            }
    )

    @PostMapping(path = "/{postId}/likes")
    public ResponseEntity<Void> likePost(@PathVariable Long postId)  {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UNLIKE POST
    @Operation(
            method = "DELETE",
            summary = "Unlike a post",
            description = "Users can unlike post",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Post unliked succesfully"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Post is already unliked"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found"
                    )
            }
    )
    @DeleteMapping(path = "/{postId}/likes")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) throws BadRequestException {
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // MAKE COMMENT
    @Operation(
            method = "POST",
            summary = "Users make comments on Posts"
    )
    @PostMapping(path = "/{postId}/comments")
    public ResponseEntity<CommentEntity> makeComment(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // DELETE COMMENT
    @Operation(
            method = "DELETE",
            summary = "Users deletes comment"
    )
    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    public ResponseEntity<CommentEntity> deleteComment(@PathVariable Long postId, @PathVariable Long commentId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            method = "POST",
            summary = "Users reply to comments"
    )
    @PostMapping(path = "/{postId}/comments/{commentId}/reply")
    public ResponseEntity<CommentEntity> replyComment(@PathVariable Long postId, @PathVariable Long commentId){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
