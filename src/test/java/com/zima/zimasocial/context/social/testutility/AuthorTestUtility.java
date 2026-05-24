package com.zima.zimasocial.context.social.testutility;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorId;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class AuthorTestUtility {
    public static AuthorDomain mockAuthor(Long authorId) {
        return new AuthorDomain(new AuthorId(authorId), "mock", "mockName", LocalDateTime.now());
    }
    public static AuthorId mockAuthorId() {
        return new AuthorId(ThreadLocalRandom.current().nextLong());
    }
    public static AuthorDomain mockPrivateAccountAuthor(){
        return new AuthorDomain(new AuthorId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", "", "","", true, "", 0, 0, LocalDateTime.now(), null);
    }

    public static AuthorDomain mockPublicAccountAuthor(){
        return new AuthorDomain(new AuthorId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", "", "","", false, "", 0, 0, LocalDateTime.now(), null);
    }
}
