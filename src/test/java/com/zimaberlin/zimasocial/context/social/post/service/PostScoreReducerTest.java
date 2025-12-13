package com.zimaberlin.zimasocial.context.social.post.service;

import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

class PostScoreReducerTest {
    private final PostScoreReducer postScoreReducer = new PostScoreReducer();
    @Test
    void testReduce_WhenPostCreatedOneHourAgo_ReduceScore() {
        Post post = Post.builder().createdAt(OffsetDateTime.now().minusHours(1)).score(100).build();
        Integer punishmentScore = postScoreReducer.calculateReduceAmount(post);
        post.punishScore(punishmentScore);
        Assertions.assertEquals(96, post.getScore());
    }

    @Test
    void testReduce_WhenPostCreatedPassedThreeDays_DontReduceScore() {
        Post post = Post.builder().createdAt(OffsetDateTime.now().minusHours(73)).score(50).build();
        Integer punishmentScore = postScoreReducer.calculateReduceAmount(post);
        post.punishScore(punishmentScore);
        Assertions.assertEquals(50, post.getScore());
    }

    @Test
    void testReduce_WhenPostCreatedYesterday_AndScoreIsDecimalConvertToInteger() {
        Post post = Post.builder().createdAt(OffsetDateTime.now().minusHours(1)).score(41).build();
        Integer punishmentScore = postScoreReducer.calculateReduceAmount(post);
        post.punishScore(punishmentScore);
        Assertions.assertEquals(40, post.getScore());
    }
}