package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.views.user.BasicUserView;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.views.user.DetailedUserView;

public class CustomUserMapper {
    public static BasicUserView entityToDomain(UserEntity entity){
        BasicUserView basicUserView = new BasicUserView();
        basicUserView.setSlug(entity.getSlug());
        basicUserView.setName(entity.getName());
        basicUserView.setFamilyName(entity.getFamilyName());
        basicUserView.setAvatarUrl(entity.getAvatarUrl());
        basicUserView.setBio(entity.getBio());
        return basicUserView;
    }
}
