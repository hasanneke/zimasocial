package com.zimaberlin.zimasocial.views.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zimaberlin.zimasocial.controller.PostController;
import com.zimaberlin.zimasocial.controller.UserController;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.views.user.UserView;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostView extends RepresentationModel<PostView> {
    private Long id;
    private String content;
    private String url;
    private PostType type;
    private int likeCount = 0;
    private int commentCount = 0;
    private boolean isLiked = false;
    private UserView user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Static builder class that overrides the Lombok builder

    public PostView addLinks() {
        this.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PostController.class)
                        .getPost(this.getId())
        ).withSelfRel());

        return this;
    }
}