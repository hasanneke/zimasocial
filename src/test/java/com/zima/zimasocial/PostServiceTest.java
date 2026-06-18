//package com.zima.zimasocial;
//
//import com.zima.zimasocial.config.userdetails.CustomUserDetails;
//import com.zima.zimasocial.entity.PostEntity;
//import com.zima.zimasocial.entity.PostType;
//import com.zima.zimasocial.entity.user.UserEntity;
//import com.zima.zimasocial.repository.CommentRepository;
//import com.zima.zimasocial.repository.LikeRepository;
//import com.zima.zimasocial.repository.PostRepository;
//import com.zima.zimasocial.repository.UserRepository;
//import com.zima.zimasocial.service.notification.NotificationService;
//import com.zima.zimasocial.context.social.post.api.payload.PostPayload;
//import com.zima.zimasocial.service.posts.PostService;
//import com.zima.zimasocial.service.posts.PostServiceImpl;
//import com.zima.zimasocial.context.social2.post.api.views.PostView;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//@ExtendWith(MockitoExtension.class)
//public class PostServiceTest {
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//
//    @Mock
//    private LikeRepository likeRepository;
//
//    @Mock
//    private NotificationService notificationService;
//
//    private PostService postService;
//
//    @BeforeEach
//    void setUp(){
//        postService = new PostServiceImpl(
//                postRepository,
//                likeRepository,
//                commentRepository,
//                userRepository,
//                eventPublisher,
//                notificationService
//        );
//
//        SecurityContext securityContext = mock(SecurityContext.class);
//        Authentication authentication = mock(Authentication.class);
//
//        UserEntity testUser = new UserEntity();
//        testUser.setId(1L);
//        testUser.setSlug("testuser");
//        testUser.setEmail("test@example.com");
//
//        CustomUserDetails userDetails = new CustomUserDetails(testUser);
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
//    }
//
//    @Test
//    public void createPostSuccess(){
//        PostPayload postPayload = new PostPayload();
//        postPayload.setContent("Test post");
//        postPayload.setType(PostType.any);
//        postPayload.setUrl("google.com");
//
//        UserEntity testUser = new UserEntity();
//        testUser.setId(1L);
//
//        PostEntity savedPost = PostEntity.builder()
//                .url(postPayload.getUrl())
//                .content(postPayload.getContent())
//                .user(testUser)
//                .type(postPayload.getType()).build();
//
//        when(postRepository.save(any(PostEntity.class))).thenReturn(savedPost);
//
//        PostView postView = postService.createPost(postPayload);
//
//        // Verify the results
//        assertEquals(postPayload.getContent(), postView.getContent());
//        assertEquals(postPayload.getType(), postView.getType());
//        assertEquals(postPayload.getUrl(), postView.getUrl());
//    }
//}
