package com.zima.zimasocial.shared;

import com.zima.zimasocial.config.userdetails.CustomUserDetails;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    public static Account getLoggedAccount() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getAccount();
    }

    public static AuthorId getCurrentUserId() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new AuthorId(principal.getAccount().getAccountId().getValue());
    }
}
