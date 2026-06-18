package com.zima.zimasocial.context.account.value;

import java.util.Set;

public record CreateAccount(String email, String name, String familyName, String authProvider, Set<UserRole> roles, String slug) {
}
