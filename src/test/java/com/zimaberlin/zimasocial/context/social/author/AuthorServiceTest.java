package com.zimaberlin.zimasocial.context.social.author;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zimaberlin.zimasocial.TestUtil;
import com.zimaberlin.zimasocial.context.social.author.application.AuthorService;
import com.zimaberlin.zimasocial.context.social.author.entity.Author;
import com.zimaberlin.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zimaberlin.zimasocial.context.social.author.repository.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import com.zimaberlin.zimasocial.context.social.authorrelation.*;
import com.zimaberlin.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zimaberlin.zimasocial.context.social.image.ImageService;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private StaticEventPublisher staticEventPublisher;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorRelationCollection authorRelationRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private FollowRequestCollection followRequestCollection;
    @Mock
    private AuthorRelationCollection authorRelationCollection;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void requestToFollowAuthor_WhenAuthorNotFound_ThrowAuthorNotFoundException() {
        when(authorRepository.findBySlugAndIsDisabledFalse(any(String.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthorNotFoundException.class, () -> authorService.requestToFollow(""));
    }

    @Test
    void requestToFollowAuthor_WhenAuthorFoundAndFollowRequestSuccess_ThenSaveFollowRequestToCollection() {
        Author followerAuthor = new Author(new AuthorId(0L), "", "", LocalDateTime.now());
        Author followedAuthor = new Author(new AuthorId(1L), "", "", LocalDateTime.now());
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(followerAuthor);
        when(authorRepository.findBySlugAndIsDisabledFalse(any(String.class))).thenReturn(Optional.of(followedAuthor));

        authorService.requestToFollow("");

        ArgumentCaptor<FollowRequest> followRequestArgumentCaptor = ArgumentCaptor.forClass(FollowRequest.class);
        verify(followRequestCollection).save(followRequestArgumentCaptor.capture());

        FollowRequest followRequest = followRequestArgumentCaptor.getValue();

        Assertions.assertEquals(followRequest.getFollowedAuthorId(), new AuthorId(1L));
        Assertions.assertNotNull(followRequest.getId());
        Assertions.assertEquals(followerAuthor.getId(), new AuthorId(0L));
        Assertions.assertNotNull(followRequest.getCreatedAt());
        Assertions.assertFalse(followRequest.getIsAccepted());
    }

    @Test
    void testApproveFollowRequest() {
        String followerSlug = "followerSlug";
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(TestUtil.mockAuthor(1L));
        when(authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(followerSlug)).thenReturn(Optional.of(TestUtil.mockAuthor(0L)));
        FollowRequest followRequest = new FollowRequest(UUID.randomUUID(), new AuthorId(1L), new AuthorId(0L), false, LocalDateTime.now(), null);
        when(followRequestCollection.findByFollowedIdAndFollowerId(any(AuthorId.class),any(AuthorId.class))).thenReturn(Optional.of(followRequest));
        authorService.acceptFollowRequest(followerSlug);
        verify(followRequestCollection).delete(followRequest);
    }

    @Test
    void testDeclineFollowRequest() {
        when(authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked("2")).thenReturn(Optional.of(TestUtil.mockAuthor(1L, "1")));
        when(authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked("1")).thenReturn(Optional.of(TestUtil.mockAuthor(0L, "2")));
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(TestUtil.mockAuthor(1L, "1"));
        UUID id = UuidCreator.getTimeOrdered();
        FollowRequest followRequest = new FollowRequest(id, new AuthorId(0L), new AuthorId(1L), false, LocalDateTime.now(), null);
        when(followRequestCollection.findByFollowedIdAndFollowerId(any(), any())).thenReturn(Optional.of(followRequest));
        authorService.deleteFollowRequest("1", "2");
        verify(followRequestCollection).delete(followRequest);
    }
}
