package com.zima.zimasocial.context.social.api.author;

import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.views.post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorView {
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

    public AuthorView(PostDTO postDTO) {
        this.slug = postDTO.getSlug();
        this.name = postDTO.getName();
        this.familyName = postDTO.getName();
        this.avatarUrl = postDTO.getAvatarFileName();
        this.bio = postDTO.getBio();
        this.followerCount = postDTO.getFollowerCount().intValue();
        this.followingCount = postDTO.getFollowingCount().intValue();
        this.isFollowed = postDTO.getIsFollowed();
        this.isFollowingMe = postDTO.getIsFollowingMe();
        this.isFollowRequestSent = postDTO.getIsFollowRequestSent();
        this.isFollowRequestReceived = postDTO.getIsFollowRequestReceived();
    }
}
