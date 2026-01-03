package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

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
    private boolean isFollowingMe;
    private boolean isFollowRequestSent;
    private boolean isFollowRequestReceived;
    private Boolean isPrivate;
    private Boolean isBlocked;
    private Boolean termsOfUseAccepted;
    private List<PlaylistDTO> playlists;

    public AuthorView addLinks() {
        // Add self link to user details
        this.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(AuthorController.class)
                                .getUser(this.slug))
                .withSelfRel());
        // Add link to followers
        try {
            this.add(WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(AuthorController.class)
                                    .getFollowers(this.slug, 0, 20))
                    .withRel("followers"));
            this.add(WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(AuthorController.class)
                                    .getFollowing(this.slug, 0, 20))
                    .withRel("following"));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
