package com.zima.zimasocial;

import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;

public final class AuthorFixture {

    private AuthorFixture() {}

    public static Author validAuthor() {
        return Author.create(
                new AuthorId(1L),
                "test-author",
                "author@example.com",
                "Test",
                "Author"
        );
    }

    public static Author privateAuthor() {
        Author author = validAuthor();
        author.makePrivate(); // better than setting field directly
        return author;
    }
}