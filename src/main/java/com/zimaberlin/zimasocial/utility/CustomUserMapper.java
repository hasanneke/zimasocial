package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.views.user.UserView;
import com.zimaberlin.zimasocial.entity.user.UserEntity;

public class CustomUserMapper {
    public static UserView entityToDomain(UserEntity entity)  {
        UserView userView = new UserView();
        userView.setId(entity.getId());
        userView.setSlug(entity.getSlug());
        userView.setName(entity.getName());
        userView.setFamilyName(entity.getFamilyName());
        userView.setAvatarUrl(entity.getAvatarUrl());
        userView.setBio(entity.getBio());
        userView.setFollowerCount(entity.getFollowersCount());
        userView.setFollowingCount(entity.getFollowingCount());
        userView.addLinks();
        return userView;
    }
}
