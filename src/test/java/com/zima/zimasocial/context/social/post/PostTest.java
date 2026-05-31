package com.zima.zimasocial.context.social.post;

import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.PostFixture;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.value.CommentId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
class PostTest {
    @Test
    void testLike_WhenOtherAuthorLikes_IncrementLikeCountAndIncreaseScore() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Post post = PostFixture.validPost();
        Author author = AuthorFixture.validAuthor();
        post.like(author.getId());
        Assertions.assertEquals(102, post.getStats().getScore());
        Assertions.assertEquals(1, post.getStats().getLikeCount());
    }

    @Test
    void testLike_WhenOtherAuthorUnlikes_DecrementLikeCountAndDecrementScore() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Post post = PostFixture.validPost();
        Author author = AuthorFixture.validAuthor();
        post.unlike(author.getId());
        Assertions.assertEquals(98, post.getStats().getScore());
        Assertions.assertEquals(0, post.getStats().getLikeCount());
    }

    @Test
    void testLike_WhenAuthorSelfLikes_IncrementLikeCountAndScoreNotChanged() {
        Post post = PostFixture.validPost();
        post.like(post.getAuthorId());
        Assertions.assertEquals(100, post.getStats().getScore());
        Assertions.assertEquals(1, post.getStats().getLikeCount());
    }

    @Test
    void testLike_WhenAuthorSelfUnlikes_DecrementLikeCountAndScoreNotChanged() {
        Post post = PostFixture.validPost();
        post.unlike(post.getAuthorId());
        Assertions.assertEquals(100, post.getStats().getScore());
        Assertions.assertEquals(0, post.getStats().getLikeCount());
    }
    @Test
    void testComment_WhenOtherAuthorComments_IncreaseScoreAndCommentCount() {
        Post post = PostFixture.validPost();
        post.comment(new CommentId(new Random().nextLong()),post.getAuthorId(), "", null, true);
        Assertions.assertEquals(105, post.getStats().getScore());
        Assertions.assertEquals(1, post.getStats().getCommentCount());
    }

    @Test
    void testComment_WhenOtherAuthorRemovesComment_DecreaseScoreAndCommentCount() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Post post = PostFixture.validPost();
        Author author = AuthorFixture.validAuthor();
        post.removeComment(author.getId(), true);
        Assertions.assertEquals(95, post.getStats().getScore());
    }

    @Test
    void testComment_WhenAuthorSelfComments_ScoreNotChangedAndCommentCountIncreased() {
        Post post = PostFixture.validPost();
        post.comment(new CommentId(new Random().nextLong()),post.getAuthorId(), "", null, false);
        Assertions.assertEquals(100, post.getStats().getScore());
        Assertions.assertEquals(1, post.getStats().getCommentCount());
    }

    @Test
    void testComment_WhenAuthorSelfRemovesComment_ScoreNotChangesAndCommentCountDecreased() {
        Post post = PostFixture.validPost();
        post.removeComment(post.getAuthorId(), false);
        Assertions.assertEquals(100, post.getStats().getScore());
        Assertions.assertEquals(0, post.getStats().getCommentCount());
    }

    @Test
    void testPunishScore_WhenPostCreatedOneHourAgo_ReduceScore() {
        Post post = PostFixture.validPost();
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.now().minusHours(1));
        post.punishScore();
        Assertions.assertEquals(96, post.getStats().getScore());
    }

    @Test
    void testPunishScore_WhenPostCreatedTwoHoursAgo_ReduceScore() {
        Post post = PostFixture.validPost();
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.now().minusHours(2));
        post.punishScore();
        Assertions.assertEquals(93, post.getStats().getScore());
    }

    @Test
    void testPunishScore_WhenPostCreated24Ago_ReduceScore() {
        Post post = PostFixture.validPost();
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.now().minusDays(1));
        post.punishScore();
        Assertions.assertTrue(post.getStats().getScore() < 50);
    }

    @Test
    void testPunishScore_WhenPostCreatedPassedThreeDays_DontReduceScore() {
        Post post = PostFixture.validPost();
        ReflectionTestUtils.setField(post, "createdAt", LocalDateTime.now().minusDays(3));
        post.punishScore();
        Assertions.assertEquals(100, post.getStats().getScore());
    }
}