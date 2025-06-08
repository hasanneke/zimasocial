package com.zimaberlin.zimasocial.views.post;

import com.zimaberlin.zimasocial.context.social.api.author.AuthorView;
import com.zimaberlin.zimasocial.controller.PostController;
import com.zimaberlin.zimasocial.entity.PostType;
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
    private AuthorView user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isReported;
    private Boolean isVisible;
    public PostView addLinks() {
        this.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PostController.class)
                        .getPost(this.getId())
        ).withSelfRel());

        return this;
    }
}