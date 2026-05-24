package com.zima.zimasocial;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.entity.MediaType;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

public class TestUtil {
    public static AuthorDomain mockAuthor(Long authorId){
        return new AuthorDomain(new AuthorDomainId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }
    public static AuthorDomain mockAuthor(Long authorId, String slug){
        return new AuthorDomain(new AuthorDomainId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }

    public static PostDomain mockPostForScoring(LocalDateTime createdAt, LocalDateTime lastPunishedAt, Integer score) {
        Clock clock = Clock.system(ZoneId.systemDefault());
        return PostDomain.reconstitute(new Random().nextLong(), mockAuthor(new Random().nextLong()).getId(), new PostContent("", MediaType.any, null), 0, 0, score, false, createdAt, null, lastPunishedAt);
    }

    public static PostDomain mockAnyPost() {
        return PostDomain.reconstitute(new Random().nextLong(), mockAuthor(new Random().nextLong()).getId(), new PostContent("", MediaType.any, null), 0, 0, 100, false, null, null, null);
    }
    public static PostDomain mockAnyPost(AuthorDomainId authorId) {
        return PostDomain.reconstitute(new Random().nextLong(), authorId, new PostContent("", MediaType.any, null), 0, 0, 100, false, null, null, null);
    }
}
