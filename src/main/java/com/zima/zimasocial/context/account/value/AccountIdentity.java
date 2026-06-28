package com.zima.zimasocial.context.account.value;

import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.service.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class AccountIdentity {
    private final AccountId accountId;
    private final String email;
    private final String slug;
    private final LoginType loginType;
    private final Set<UserRole> roles;
}
