//package com.zimaberlin.zimasocial;
//
//import com.zimaberlin.zimasocial.config.CustomUserDetails;
//import com.zimaberlin.zimasocial.entity.PostEntity;
//import com.zimaberlin.zimasocial.entity.PostType;
//import com.zimaberlin.zimasocial.entity.UserEntity;
//import com.zimaberlin.zimasocial.repository.CommentRepository;
//import com.zimaberlin.zimasocial.repository.LikeRepository;
//import com.zimaberlin.zimasocial.repository.PostRepository;
//import com.zimaberlin.zimasocial.repository.UserRepository;
//import com.zimaberlin.zimasocial.service.notification.NotificationService;
//import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
//import com.zimaberlin.zimasocial.service.posts.PostService;
//import com.zimaberlin.zimasocial.service.posts.PostServiceImpl;
//import com.zimaberlin.zimasocial.views.post.PostView;
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
