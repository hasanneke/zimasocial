package com.zimaberlin.zimasocial.context.social.api.view;
import com.zimaberlin.zimasocial.controller.UserController;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorView extends RepresentationModel<AuthorView> {
    private Long id;
    private String slug;
    private String name;
    private String familyName;
    private String avatarUrl;
    private String bio;
    private int followerCount;
    private int followingCount;
    private boolean isFollowed;
    private Boolean isPrivate;
    private Boolean isBlocked;

    public AuthorView addLinks() {
        // Add self link to user details
        this.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUser(this.slug))
                .withSelfRel());
        // Add link to followers
        try {
            this.add(WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(UserController.class)
                                    .getFollowers(this.slug, 0, 20))
                    .withRel("followers"));
            this.add(WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(UserController.class)
                                    .getFollowing(this.slug, 0, 20))
                    .withRel("following"));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
