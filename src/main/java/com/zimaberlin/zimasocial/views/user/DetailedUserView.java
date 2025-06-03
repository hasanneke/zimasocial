package com.zimaberlin.zimasocial.views.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DetailedUserView extends UserView {
    private String email;
    public void mergeUserView(UserView userView) {
        setId(userView.getId());
        setSlug(userView.getSlug());
        setName(userView.getName());
        setFamilyName(userView.getFamilyName());
        setAvatarUrl(userView.getAvatarUrl());
        setBio(userView.getBio());
        setFollowerCount(userView.getFollowerCount());
        setFollowingCount(userView.getFollowingCount());
        setFollowed(userView.isFollowed());
        setIsPrivate(userView.getIsPrivate());
        setIsBlocked(userView.getIsBlocked());
        addLinks();
    }
}
