package com.zima.zimasocial.utility;

import com.zima.zimasocial.config.CustomUserDetails;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    @Autowired
    private UserJpaRepository userRepository;

    public static UserEntity getCurrentUserProfile() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getProfile();
    }
}
