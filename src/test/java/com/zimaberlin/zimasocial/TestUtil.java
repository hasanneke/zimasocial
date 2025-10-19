package com.zimaberlin.zimasocial;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;

public class TestUtil {
    public static Author mockAuthor(Long authorId){
        return new Author(new AuthorId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }
    public static Author mockAuthor(Long authorId, String slug){
        return new Author(new AuthorId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }
}
