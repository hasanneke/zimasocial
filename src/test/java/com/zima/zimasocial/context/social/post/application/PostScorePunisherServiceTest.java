package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostScorePunisherServiceTest {
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostScorePunisherService postScorePunisherService;

    @Test
    void punishPosts() {
        LocalDateTime start = LocalDate.now().minusDays(3).atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();
        Post post1 = mock(Post.class);
        Post post2 =  mock(Post.class);
        Post post24 = mock(Post.class);
        Post post72 =  mock(Post.class);
        List<Post> posts = List.of(post1, post2, post24, post72);
        when(postRepository.findAllByCreatedAtBetween(start, end)).thenReturn(posts);
        postScorePunisherService.punishPosts();
        for (Post post : posts) {
            verify(post).punishScore();
            verify(postRepository).save(post);
        }
    }
}