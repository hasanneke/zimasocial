package com.zima.zimasocial.context.social.post;

import com.zima.zimasocial.TestUtil;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.comment.Comment;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class PostTest {
    @Mock
    private  ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private StaticEventPublisher staticEventPublisher;
    @Test
    void testLike_WhenOtherAuthorLikes_IncrementLikeCountAndIncreaseScore() {
        AuthorId otherAuthor = new AuthorId(102L);
        AuthorId ownerAuthor = new AuthorId(100L);
        Post post = dummyAnyPost(ownerAuthor);
        post.like(otherAuthor);
        Assertions.assertEquals(102, post.getScore());
        Assertions.assertEquals(1, post.getLikeCount());
    }

    @Test
    void testLike_WhenOtherAuthorUnlikes_DecrementLikeCountAndDecrementScore() {
        AuthorId otherAuthor = new AuthorId(102L);
        AuthorId ownerAuthor = new AuthorId(100L);
        Post post = dummyAnyPost(ownerAuthor);
        post.unliked(otherAuthor);
        Assertions.assertEquals(98, post.getScore());
        Assertions.assertEquals(-1, post.getLikeCount());
    }

    @Test
    void testLike_WhenAuthorSelfLikes_IncrementLikeCountAndScoreNotChanged() {
        AuthorId author = new AuthorId(100L);
        AuthorId ownerAuthor = new AuthorId(100L);
        Post post = dummyAnyPost(ownerAuthor);
        post.like(author);
        Assertions.assertEquals(100, post.getScore());
        Assertions.assertEquals(1, post.getLikeCount());
    }

    @Test
    void testLike_WhenAuthorSelfUnlikes_DecrementLikeCountAndScoreNotChanged() {
        AuthorId author = new AuthorId(100L);
        AuthorId ownerAuthor = new AuthorId(100L);
        Post post = dummyAnyPost(ownerAuthor);
        post.unliked(author);
        Assertions.assertEquals(100, post.getScore());
        Assertions.assertEquals(-1, post.getLikeCount());
    }
    @Test
    void testComment_WhenOtherAuthorComments_IncreaseScoreAndCommentCount() {
        AuthorId author = new AuthorId(102L);
        AuthorId commenter = new AuthorId(100L);
        Post post = dummyAnyPost(author);
        post.comment(commenter, "");
        Assertions.assertEquals(105, post.getScore());
        Assertions.assertEquals(1, post.getCommentCount());
    }

    @Test
    void testComment_WhenOtherAuthorRemovesComment_DecreaseScoreAndCommentCount() {
        AuthorId author = new AuthorId(102L);
        AuthorId commenter = new AuthorId(100L);
        Post post = dummyAnyPost(author);
        post.removeComment(commenter);
        Assertions.assertEquals(95, post.getScore());
        Assertions.assertEquals(-1, post.getCommentCount());
    }

    @Test
    void testComment_WhenAuthorSelfComments_ScoreNotChangedAndCommentCountIncreased() {
        AuthorId author = new AuthorId(100L);
        AuthorId commenter = new AuthorId(100L);
        Post post = dummyAnyPost(author);
        post.comment(commenter, "");
        Assertions.assertEquals(100, post.getScore());
        Assertions.assertEquals(1, post.getCommentCount());
    }

    @Test
    void testComment_WhenAuthorSelfRemovesComment_ScoreNotChangesAndCommentCountDecreased() {
        AuthorId author = new AuthorId(100L);
        AuthorId commenter = new AuthorId(100L);
        Post post = dummyAnyPost(author);
        post.removeComment(commenter);
        Assertions.assertEquals(100, post.getScore());
        Assertions.assertEquals(-1, post.getCommentCount());
    }

    @Test
    void testPunishScore_WhenPostCreatedOneHourAgo_ReduceScore() {
        Post post = TestUtil.mockPostForScoring(LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1), 100);
        post.punishScore();
        Assertions.assertEquals(96, post.getScore());
    }

    @Test
    void testPunishScore_WhenPostCreatedTwoHoursAgo_ReduceScore() {
        Post post = TestUtil.mockPostForScoring(LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(2), 100);
        post.punishScore();
        Assertions.assertEquals(93, post.getScore());
    }

    @Test
    void testPunishScore_WhenPostCreated24Ago_ReduceScore() {
        Post post = TestUtil.mockPostForScoring(LocalDateTime.now().minusHours(1),LocalDateTime.now().minusHours(24), 100);
        post.punishScore();
        Assertions.assertTrue(post.getScore() < 50);
    }

    @Test
    void testPunishScore_WhenPostCreatedPassedThreeDays_DontReduceScore() {
        Post post = TestUtil.mockPostForScoring(LocalDateTime.now().minusHours(73), LocalDateTime.now().minusHours(73), 50);
        post.punishScore();
        Assertions.assertEquals(50, post.getScore());
    }

    private static Post dummyAnyPost(AuthorId ownerAuthorId) {
        return TestUtil.mockAnyPost(ownerAuthorId);
    }

    private static Comment dummyComment(AuthorId authorId) {
        return new Comment(0L,  null, authorId, null);
    }
}