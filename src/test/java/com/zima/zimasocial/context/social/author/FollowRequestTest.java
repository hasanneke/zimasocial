package com.zima.zimasocial.context.social.author;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.authorrelation.values.AuthorFollowRequestAcceptedEvent;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.context.social.authorrelation.FollowRequestAlreadyProcessed;
import com.zima.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class FollowRequestTest {
    @Test
    void testApprove_WhenAccepted_SetIsAcceptedTrueAndUpdateUpdatedAt() {
        try(MockedStatic<StaticEventPublisher> mockedStatic = Mockito.mockStatic(StaticEventPublisher.class)){
            FollowRequest followRequest = new FollowRequest(UuidCreator.getTimeOrdered(), new AuthorId(0L),new AuthorId(1L), false, LocalDateTime.now());
            followRequest.accept();
            Assertions.assertTrue(followRequest.getIsAccepted());
            Assertions.assertNotNull(followRequest.getUpdatedAt());
            mockedStatic.verify(() -> StaticEventPublisher.publishEvent(new AuthorFollowRequestAcceptedEvent( new AuthorId(0L),new AuthorId(1L))));
        }
    }
    @Test
    void testApprove_WhenUpdatedAtNotNull_ThrowFollowRequestAlreadyProcessed() {
        FollowRequest followRequest = new FollowRequest(UuidCreator.getTimeOrdered(), new AuthorId(0L),new AuthorId(0L), false, LocalDateTime.now(), LocalDateTime.now());
        Assertions.assertThrows(FollowRequestAlreadyProcessed.class, followRequest::accept);
    }
}
