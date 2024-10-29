package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.config.CustomUserDetails;
import com.zimaberlin.zimasocial.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    public static UserEntity getCurrentUserProfile() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getProfile();
    }
}
