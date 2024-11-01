package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.domain.User;
import com.zimaberlin.zimasocial.entity.UserEntity;

public class CustomUserMapper {
    public static User entityToDomain(UserEntity entity){
        User user = new User();
        user.setSlug(entity.getSlug());
        user.setName(entity.getName());
        user.setFamilyName(entity.getFamilyName());
        user.setAvatarUrl(entity.getAvatarUrl());
        return user;
    }
}
