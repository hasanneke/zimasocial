package com.zima.zimasocial.context.account.factory;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.value.AccountIdentity;
import com.zima.zimasocial.context.account.value.OAuthTokenResult;
import com.zima.zimasocial.context.account.value.PersonalInfo;
import com.zima.zimasocial.context.account.value.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountFactory {
    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public Account createOAuth2Account(OAuthTokenResult tokenResult, String provider) throws Exception {
        AccountIdentity accountIdentity = AccountIdentity
                .builder()
                .accountId(new AccountId(accountRepository.nextId()))
                .email(tokenResult.getEmail())
                .authProvider(provider)
                .slug(generateUniqueSlug(tokenResult.getName()))
                .roles(Set.of(UserRole.regular))
                .build();

        PersonalInfo personalInfo = PersonalInfo
                .builder()
                .name(tokenResult.getName())
                .surname(tokenResult.getSurname())
                .build();

        return Account.newAccount(accountIdentity, personalInfo);
    }

    public Account createZimaAccount() {
        String email = "zimablue0@example.com";
        String name = "Zima Blue";
        String familyName = "Blue";

        String slug = generateUniqueSlug(name);
        AccountIdentity accountIdentity = AccountIdentity
                .builder()
                .accountId(new AccountId(accountRepository.nextId()))
                .email(email)
                .authProvider("test")
                .slug(slug)
                .roles(Set.of(UserRole.regular))
                .build();
        PersonalInfo personalInfo = PersonalInfo
                .builder()
                .name(name)
                .surname(familyName)
                .build();

        return  Account.newAccount(accountIdentity, personalInfo);
    }

    private String generateUniqueSlug(String name) {
        String slug = getTrimmedName(name);

        while (accountRepository.findBySlugWithDeletedUsers(slug).isPresent()) {
            slug = slug + random.nextInt(10000000);
        }

        return slug;
    }
    private String getTrimmedName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        return name.replaceAll("\\s+", "").toLowerCase();
    }
}
