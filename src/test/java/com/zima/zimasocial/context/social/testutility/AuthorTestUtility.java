package com.zima.zimasocial.context.social.testutility;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class AuthorTestUtility {
    public static AuthorDomain mockAuthor(Long authorId) {
        return new AuthorDomain(new AuthorDomainId(authorId), "mock", "mockName", LocalDateTime.now());
    }
    public static AuthorDomainId mockAuthorId() {
        return new AuthorDomainId(ThreadLocalRandom.current().nextLong());
    }
    public static AuthorDomain mockPrivateAccountAuthor(){
        return new AuthorDomain(new AuthorDomainId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", "", "","", true, "", 0, 0, LocalDateTime.now(), null);
    }

    public static AuthorDomain mockPublicAccountAuthor(){
        return new AuthorDomain(new AuthorDomainId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", "", "","", false, "", 0, 0, LocalDateTime.now(), null);
    }
}
