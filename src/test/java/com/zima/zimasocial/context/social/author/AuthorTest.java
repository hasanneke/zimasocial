package com.zima.zimasocial.context.social.author;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.account.exception.CircularFollowException;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorId;
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
public class AuthorTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private StaticEventPublisher staticEventPublisher;
    @Test
    void requestToFollow_WhenUserSendFollowRequestToSelf_ThrowCircularFollowException() {
        AuthorDomain followerAuthor = new AuthorDomain(new AuthorId(0L), "", "", LocalDateTime.now());
        AuthorDomain followedAuthor = new AuthorDomain(new AuthorId(0L), "", "", LocalDateTime.now());
        Assertions.assertThrows(CircularFollowException.class, ()->followedAuthor.requestToFollow(followerAuthor.getId(), UuidCreator.getTimeOrdered()));
    }

    @Test
    void requestToFollow_WhenAuthorFollowRequestSent_SendAuthorFollowRequestSentEvent() {
        AuthorDomain followerAuthor = new AuthorDomain(new AuthorId(0L), "", "", LocalDateTime.now());
        AuthorDomain followedAuthor = new AuthorDomain(new AuthorId(1L), "", "", LocalDateTime.now());
        followedAuthor.requestToFollow(followerAuthor.getId(), UuidCreator.getTimeOrdered());
    }
}
