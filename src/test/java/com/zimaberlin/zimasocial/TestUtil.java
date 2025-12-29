package com.zimaberlin.zimasocial;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.context.social.post.value.PostContent;
import com.zimaberlin.zimasocial.entity.PostType;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Random;

public class TestUtil {
    public static Author mockAuthor(Long authorId){
        return new Author(new AuthorId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }
    public static Author mockAuthor(Long authorId, String slug){
        return new Author(new AuthorId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }

    public static Post mockPostForScoring(OffsetDateTime createdAt, OffsetDateTime lastPunishedAt, Integer score) {
        Clock clock = Clock.system(ZoneId.systemDefault());
        return Post.reconstitute(new Random().nextLong(), mockAuthor(new Random().nextLong()).getId(), new PostContent("", PostType.any, null), 0, 0, score, false, createdAt, null, lastPunishedAt);
    }

    public static Post mockAnyPost() {
        return Post.reconstitute(new Random().nextLong(), mockAuthor(new Random().nextLong()).getId(), new PostContent("", PostType.any, null), 0, 0, 100, false, null, null, null);
    }
}
