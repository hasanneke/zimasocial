package com.zimaberlin.zimasocial.context.social.testutility;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class AuthorTestUtility {
    public static Author mockAuthor(Long authorId) {
        return new Author(new AuthorId(authorId), "mock", "mockName", LocalDateTime.now());
    }
    public static AuthorId mockAuthorId() {
        return new AuthorId(ThreadLocalRandom.current().nextLong());
    }
    public static Author mockPrivateAccountAuthor(){
        return new Author(new AuthorId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", "", "","", true, "", 0, 0, LocalDateTime.now(), null);
    }

    public static Author mockPublicAccountAuthor(){
        return new Author(new AuthorId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", "", "","", false, "", 0, 0, LocalDateTime.now(), null);
    }
}
