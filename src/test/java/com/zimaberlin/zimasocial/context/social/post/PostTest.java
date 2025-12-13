package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.context.social.post.entity.PostFactory;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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

    private static Post dummyAnyPost(AuthorId ownerAuthorId) {
        return PostFactory.newAnyPost(0L, "", ownerAuthorId);
    }

    private static Comment dummyComment(AuthorId authorId) {
        return new Comment(0L,  null, authorId, null);
    }
}