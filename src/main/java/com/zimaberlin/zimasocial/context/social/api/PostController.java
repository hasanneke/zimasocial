package com.zimaberlin.zimasocial.context.social.api;

import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.views.post.PostView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping(path = "/api/v2/posts")
public class PostController {
//    private final PostControllerBridge postControllerBridge;
    @Autowired
    public PostController(PostControllerBridge postControllerBridge) {
//        this.postControllerBridge = postControllerBridge;
    }

//    @PostMapping
//    public ResponseEntity<PostView> createPost(@Valid @RequestBody PostPayload payload) {
////        return ResponseEntity.ok(postControllerBridge.createPost(payload));
//    }
}
