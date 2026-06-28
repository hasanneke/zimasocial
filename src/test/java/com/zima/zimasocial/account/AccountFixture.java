package com.zima.zimasocial.account;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.service.LoginType;
import com.zima.zimasocial.context.account.value.AccountIdentity;
import com.zima.zimasocial.context.account.value.PersonalInfo;

import java.util.Random;
import java.util.Set;

public class AccountFixture {
    public static Account newAccount() {
        return Account.newAccount(createDefaultIdentity("john-doe"), createDefaultInfo());
    }
    private static AccountIdentity createDefaultIdentity(String slug) {
        return AccountIdentity.builder()
                .accountId(createId())
                .email("test@zima.com")
                .slug(slug)
                .loginType(LoginType.google)
                .roles(Set.of())
                .build();
    }

    private static PersonalInfo createDefaultInfo() {
        return PersonalInfo.builder()
                .name("John")
                .surname("Doe")
                .build();
    }

    private static AccountId createId() {
        return new AccountId(new Random().nextLong(1L));
    }
}
