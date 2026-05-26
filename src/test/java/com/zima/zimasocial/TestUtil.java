package com.zima.zimasocial;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;

import java.time.LocalDateTime;

public class TestUtil {
    public static AuthorDomain mockAuthor(Long authorId){
        return new AuthorDomain(new AuthorDomainId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }
    public static AuthorDomain mockAuthor(Long authorId, String slug){
        return new AuthorDomain(new AuthorDomainId(authorId), "mockSlug", "mockName", LocalDateTime.now());
    }
}
