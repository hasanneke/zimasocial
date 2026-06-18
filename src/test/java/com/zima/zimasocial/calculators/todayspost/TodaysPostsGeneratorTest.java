//package com.zima.zimasocial.calculators.todayspost;
//
//import com.zima.zimasocial.calculators.PostScoreCalculator;
//import com.zima.zimasocial.context.social2.post.domain.TodaysPost;
//import com.zima.zimasocial.context.social2.post.repository.PostRepository;
//import com.zima.zimasocial.context.social2.post.repository.TodaysPostRepository;
//import com.zima.zimasocial.context.social.media.value.MediaType;
//import com.zima.zimasocial.entity.PostJpaEntity;
//import com.zima.zimasocial.entity.todayspost.TodaysPostDomain;
//import com.zima.zimasocial.entity.user.UserEntity;
//import com.zima.zimasocial.entity.user.UserFactory;
//import com.zima.zimasocial.repository.PostJpaRepository;
//import com.zima.zimasocial.repository.TodaysPostRepositoryDomain;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class TodaysPostsGeneratorTest {
//    @Mock
//    private PostScoreCalculator postScoreCalculator;
//    @Mock
//    private PostRepository postJpaRepository;
//    @Mock
//    private TodaysPostRepository todaysPostRepository;
//    @InjectMocks
//    private TodaysPostsGeneratorImpl todaysPostGenerator;
//    final List<PostJpaEntity> testPosts = new ArrayList<>();
//    final List<TodaysPostDomain> testTodaysPosts = new ArrayList<>();
//    @BeforeEach
//    void setUp() {
//        UserEntity author = UserFactory.createUser(0L);
//        UserEntity author1 = UserFactory.createUser(0L);
//        UserEntity author2 = UserFactory.createUser(0L);
//        PostJpaEntity post = new PostJpaEntity();
//        post.setId(0L);
//        post.setLikeCount(0);
//        post.setCommentCount(0);
//        post.setUser(author);
//        post.setType(MediaType.music);
//        PostJpaEntity post1 = new PostJpaEntity();
//        post1.setId(1L);
//        post1.setLikeCount(2);
//        post1.setCommentCount(2);
//        post1.setUser(author);
//        post1.setType(MediaType.music);
//        PostJpaEntity post2 = new PostJpaEntity();
//        post2.setId(2L);
//        post2.setLikeCount(0);
//        post2.setCommentCount(0);
//        post2.setUser(author1);
//        post2.setType(MediaType.movie);
//        PostJpaEntity post3 = new PostJpaEntity();
//        post3.setId(3L);
//        post3.setLikeCount(2);
//        post3.setCommentCount(2);
//        post3.setUser(author1);
//        post3.setType(MediaType.movie);
//        PostJpaEntity post4 = new PostJpaEntity();
//        post4.setId(4L);
//        post4.setLikeCount(1);
//        post4.setCommentCount(1);
//        post4.setUser(author2);
//        post4.setType(MediaType.book);
//        PostJpaEntity post5 = new PostJpaEntity();
//        post5.setId(5L);
//        post5.setLikeCount(2);
//        post5.setCommentCount(2);
//        post5.setUser(author2);
//        post5.setType(MediaType.book);
//        PostJpaEntity post6 = new PostJpaEntity();
//        post6.setId(6L);
//        post6.setLikeCount(4);
//        post6.setCommentCount(4);
//        post6.setUser(author2);
//        post6.setType(MediaType.music);
//        PostJpaEntity post7 = new PostJpaEntity();
//        post7.setId(7L);
//        post7.setLikeCount(4);
//        post7.setCommentCount(4);
//        post7.setUser(author2);
//        post7.setType(MediaType.tv);
//        testPosts.addAll(List.of(mockPost(), mockPost(), mockPost(), mockPost(), mockPost(), mockPost(), mockPost(), mockPost()));
//    }
//
//    @Test
//    void testCreateTodayPosts() {
//        List<TodaysPost> existingTodaysPosts = List.of(TodaysPostDomain.builder().id(0L).isDeleted(false).build(), TodaysPostDomain.builder().id(1L).isDeleted(false).build());
//        when(postJpaRepository.findAllByCreatedAtBetween(any(), any())).thenReturn(testPosts);
//        when(todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1))).thenReturn(existingTodaysPosts);
//        todaysPostGenerator.createTodaysPost();
//        existingTodaysPosts.forEach(e->{
//            Assertions.assertEquals(true, e.getIsDeleted());
//        });
//        verify(todaysPostRepository).saveAll(existingTodaysPosts);
//        verify(todaysPostRepository).saveAll(todaysPostGenerator.selectTodaysPosts());
//    }
//
//    PostJpaEntity mockPost() {
//        PostJpaEntity post = new PostJpaEntity();
//        post.setId(new Random().nextLong());
//        post.setLikeCount(0);
//        post.setCommentCount(0);
//        post.setScore(100);
//        post.setUser(mockAuthor());
//        post.setType(MediaType.music);
//        return post;
//    }
//
//    UserEntity mockAuthor(){
//        UserEntity author = UserFactory.createUser(new Random().nextLong());
//        return author;
//    }
//}
