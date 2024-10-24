package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.dto.PostPayload;
import com.zimaberlin.zimasocial.entity.Post;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("api/v1/posts")
@AllArgsConstructor
@RequiredArgsConstructor
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<PagedModel<Post>> getPosts(@RequestParam(name = "page", defaultValue="0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> createPost(@RequestBody PostPayload payload) {
        Post createdPost = postService.createPost(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping(path = "/{postId}")
    @HasPostAccess(idParameterName = "postId")
    public ResponseEntity deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
