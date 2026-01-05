package com.zima.zimasocial.context.account.value;

import com.zima.zimasocial.entity.UserRole;

import java.util.Set;

public record CreateAccount(String email, String name, String familyName, String authProvider, Set<UserRole> roles, String slug) {
}
