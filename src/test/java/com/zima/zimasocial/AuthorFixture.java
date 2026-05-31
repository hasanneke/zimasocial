package com.zima.zimasocial;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.Random;

public final class AuthorFixture {

    private AuthorFixture() {}

    public static Author validAuthor() {
        try{
            Constructor<Author> constructor = Author.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Author author = constructor.newInstance();
            ReflectionTestUtils.setField(author, "id", new AuthorId(new Random().nextLong()));
            ReflectionTestUtils.setField(author, "slug", "test-author");
            ReflectionTestUtils.setField(author, "email", "author@example.com");
            ReflectionTestUtils.setField(author, "name", "Test");
            ReflectionTestUtils.setField(author, "familyName", "Author");
            return author;
        }catch (Exception e){
            return null;
        }
    }

    public static Author validAuthor(AuthorId authorId) {
        Author author = AuthorFixture.validAuthor();
        ReflectionTestUtils.setField(author, "id", authorId);
        return author;
    }

    public static Author validPrivateAuthor() {
        Author author = AuthorFixture.validAuthor();
        ReflectionTestUtils.setField(author, "isPrivate", true);
        return author;
    }
}