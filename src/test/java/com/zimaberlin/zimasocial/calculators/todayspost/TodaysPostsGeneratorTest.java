package com.zimaberlin.zimasocial.calculators.todayspost;

import com.zimaberlin.zimasocial.calculators.PostScoreCalculator;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.MediaType;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.user.UserFactory;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.repository.TodaysPostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TodaysPostsGeneratorTest {
    @Mock
    private PostScoreCalculator postScoreCalculator;
    @Mock
    private PostJpaRepository postJpaRepository;
    @Mock
    private TodaysPostRepository todaysPostRepository;
    @InjectMocks
    private TodaysPostsGeneratorImpl todaysPostGenerator;
    final List<PostEntity> testPosts = new ArrayList<>();
    final List<TodaysPost> testTodaysPosts = new ArrayList<>();
    @BeforeEach
    void setUp() {
        UserEntity author = UserFactory.createUser(0L);
        UserEntity author1 = UserFactory.createUser(0L);
        UserEntity author2 = UserFactory.createUser(0L);
        PostEntity post = new PostEntity();
        post.setId(0L);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setUser(author);
        post.setType(MediaType.music);
        PostEntity post1 = new PostEntity();
        post1.setId(1L);
        post1.setLikeCount(2);
        post1.setCommentCount(2);
        post1.setUser(author);
        post1.setType(MediaType.music);
        PostEntity post2 = new PostEntity();
        post2.setId(2L);
        post2.setLikeCount(0);
        post2.setCommentCount(0);
        post2.setUser(author1);
        post2.setType(MediaType.movie);
        PostEntity post3 = new PostEntity();
        post3.setId(3L);
        post3.setLikeCount(2);
        post3.setCommentCount(2);
        post3.setUser(author1);
        post3.setType(MediaType.movie);
        PostEntity post4 = new PostEntity();
        post4.setId(4L);
        post4.setLikeCount(1);
        post4.setCommentCount(1);
        post4.setUser(author2);
        post4.setType(MediaType.book);
        PostEntity post5 = new PostEntity();
        post5.setId(5L);
        post5.setLikeCount(2);
        post5.setCommentCount(2);
        post5.setUser(author2);
        post5.setType(MediaType.book);
        PostEntity post6 = new PostEntity();
        post6.setId(6L);
        post6.setLikeCount(4);
        post6.setCommentCount(4);
        post6.setUser(author2);
        post6.setType(MediaType.music);
        testPosts.addAll(List.of(post, post1, post2, post3, post4, post5, post6));
    }
    @Test
    void testSelectedTodaysPosts() {
        when(postJpaRepository.findAllByCreatedAtBetween(any(), any())).thenReturn(testPosts);
        List<TodaysPost> todaysPosts = todaysPostGenerator.selectTodaysPosts();

        assertEquals(3, todaysPosts.size());
        assertEquals(6, todaysPosts.stream().filter(e->e.getPost().getType().equals(MediaType.music))
                .findFirst().get().getPost().getId());
        assertEquals(3, todaysPosts.stream().filter(e->e.getPost().getType().equals(MediaType.movie))
                .findFirst().get().getPost().getId());
        assertEquals(5, todaysPosts.stream().filter(e->e.getPost().getType().equals(MediaType.book))
                .findFirst().get().getPost().getId());
    }

    @Test
    void testCreateTodayPosts() {
        List<TodaysPost> existingTodaysPosts = List.of(TodaysPost.builder().id(0L).isDeleted(false).build(), TodaysPost.builder().id(1L).isDeleted(false).build());
        when(postJpaRepository.findAllByCreatedAtBetween(any(), any())).thenReturn(testPosts);
        when(todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1))).thenReturn(existingTodaysPosts);
        todaysPostGenerator.createTodaysPost();
        existingTodaysPosts.forEach(e->{
            Assertions.assertEquals(true, e.getIsDeleted());
        });
        verify(todaysPostRepository).saveAll(existingTodaysPosts);
        verify(todaysPostRepository).saveAll(todaysPostGenerator.selectTodaysPosts());
    }
}
